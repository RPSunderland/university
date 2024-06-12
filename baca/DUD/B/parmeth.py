#Rafal Plizga
import numpy as np
import numpy.linalg as la

class Fourier:
    def __init__(self, input, reals = True):
        if(not reals):
            self.N = len(input) - 1
            self.c = np.array(input, dtype = complex)
            return
        if(isinstance(input, int)):
            self.N = input
            self.c = np.zeros(self.N + 1, dtype = complex)
        else: 
            self.N = (len(input) - 1) // 2
            self.c = to_complex(np.array(input))
            
    def __add__(self, other):
        if(isinstance(other, Fourier)):
            c = self.c + other.c
            return Fourier(c, False)
        else:
            c = self.c.copy()
            c[0] += complex(other)
            return Fourier(c, False)
    
    def __radd__(self, other):
        return self + other
                
    def __sub__(self, other):
        other = -other
        return self + other
        
    def __rsub__(self, other):
        return -self + other
    
    def __mul__(self, other):
        if(isinstance(other, Fourier)):
            if(self.N == 0):
                c = self.c * other.c
                return Fourier(c, False)
            c1, c2 = self.c.copy(), other.c.copy()
            c1 *= 4. * self.N
            c2 *= 4. * self.N
            c1 = np.pad(c1, (0, self.N), mode='constant')
            c2 = np.pad(c2, (0, self.N), mode='constant')
            h = np.fft.irfft(c1) * np.fft.irfft(c2)
            new_c = np.fft.rfft(h)
            new_c = new_c * (1. / (4 * self.N))
            new_c = new_c[:self.N + 1]
            return Fourier(new_c, False)
        else: 
            c = self.c * other
            return Fourier(c, False)
        
    def __rmul__(self, other):
        return self * other
    
    def __neg__(self):
        return complex(-1) * self
    
    def rotate(self, angle): #przetestowac
        c = self.c.copy()
        exponens = np.exp(1j * 2 * np.pi * np.arange(self.N + 1) * angle)
        c *= exponens
        return Fourier(c, False)      
    
    def coeffs(self): #zwraca wektor wspolczynnikow rzeczywistych w postaci listy
        return from_complex(self.c)
    
def sin(f):
    if(f.N == 0):
        c = f.c.copy()
        c = np.sin(c)
        return Fourier(c, False)
    c = f.c.copy()
    c *= 4 * f.N
    c = np.pad(c, (0, f.N), mode='constant')
    h = np.fft.irfft(c)
    h = np.sin(h)
    new_c = np.fft.rfft(h)
    new_c = new_c * (1. / (4 * f.N))
    new_c = new_c[:f.N + 1]
    return Fourier(new_c, False) 
    
def cos(f):
    if(f.N == 0):
        c = f.c.copy()
        c = np.cos(c)
        return Fourier(c, False)
    c = f.c.copy()
    c *= 4 * f.N
    c = np.pad(c, (0, f.N), mode='constant')
    h = np.fft.irfft(c)
    h = np.cos(h)
    new_c = np.fft.rfft(h)
    new_c = new_c * (1. / (4 * f.N))
    new_c = new_c[:f.N + 1]
    return Fourier(new_c, False)

def combine_coeffs(a, b):
    N = len(a)
    coeffs = np.zeros(2 * (N - 1) + 1, dtype=float)
    coeffs[0] = a[0]
    coeffs[1::2] = b[1:N]
    coeffs[2::2] = a[1:N]
    return coeffs

def from_complex(c):
    N = len(c) - 1
    a = np.zeros(N + 1, dtype = float)
    b = np.zeros(N + 1, dtype = float)
    a[0] = c[0].real
    a[1:] = 2 * c[1:].real
    b[1:] = -2 * c[1:].imag
    coeffs = combine_coeffs(a, b)
    return coeffs

def to_complex(coeffs):
    N = (len(coeffs) - 1) // 2
    a = np.zeros(N + 1, dtype = float)
    b = np.zeros(N + 1, dtype = float)
    c = np.zeros(N + 1, dtype = complex)
    a[0] = coeffs[0]
    a[1:] = coeffs[2::2]
    b[1:] = coeffs[1::2]
    c[0] = complex(a[0])
    c[1:] = 0.5 * (a[1:] - b[1:] * 1j)
    return c
    

def reducibility(F, DF, K, omega, P, invP, L):
    n = len(K)
    N = K[0].N
    invP = np.array(invP)
    P = np.array(P)
    
    #step 0.
    K_rot = np.array([k.rotate(omega) for k in K])
    F_K = np.array(F(K))
    E = np.array([f_k - k_rot for f_k, k_rot in zip(F_K, K_rot)])
    
    DF_K = np.array(DF(K))
    invP_rot = np.array([[point.rotate(omega) for point in p] for p in invP])
    E_red = np.dot(invP_rot, np.dot(DF_K, P)) - np.diag(L)
    E_inv = np.dot(invP, P) - np.eye(n)
    
    
    #step 1.
    eta = np.dot(-invP_rot, E)
    ksi = np.array([Fourier(N) for _ in xrange(n)])
    for i in xrange(n):
        ksi_c = np.zeros(N + 1, dtype = complex)
        for k in xrange(N + 1):
            ksi_c[k] = eta[i].c[k] / (L[i] - np.exp(2 * np.pi * 1j * k * omega))   
        ksi[i] = Fourier(ksi_c, False)
        
    #step 2.
    newK = K + np.dot(P, ksi)
    E_inv_rot = np.array([[point.rotate(omega) for point in e] for e in E_inv]) 
    E_red  = np.dot(invP_rot, np.dot(DF_K, P)) - np.diag(L) - np.dot(E_inv_rot, np.diag(L)) 
    
    #step 3.
    delta = np.zeros(n)
    Q = np.array([[Fourier(N) for _ in xrange(n)] for _ in xrange(n)])
    for i in xrange(n):
        delta[i] = E_red[i][i].c[0].real
        for j in xrange(n):
            Q_c = np.zeros(N + 1, dtype = complex)
            if L[i] == L[j]:
                Q_c[0] = 0.
                for k in xrange(1, N + 1):
                    Q_c[k] = -1. / (L[i] * (1 - np.exp(2 * np.pi * 1j * k * omega))) * E_red[i][i].c[k]
            else:
                for k in xrange(N + 1):
                    Q_c[k] = -1. / (L[i] - L[j] * np.exp(2 * np.pi * 1j * k * omega)) * E_red[i][j].c[k]
            Q[i][j] = Fourier(Q_c, False)
            
    #step 4.
    newP = P + np.dot(P, Q)
    newInvP = invP - np.dot((Q + E_inv), invP)
    newL = L + delta
                
    return newK, newP, newInvP, newL