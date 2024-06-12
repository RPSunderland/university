#Rafal Plizga


import numpy as np
from itertools import product
import math


#functions
def add_polynomials(P, Q):   #P + Q
    np.trim_zeros(P, 'b')
    np.trim_zeros(Q, 'b')
    max_len = max(len(P), len(Q))
    result = np.zeros(max_len)
    for i in xrange(max_len):
        if i < len(P):
            result[i] += P[i]
        if i < len(Q):
            result[i] += Q[i]
    return np.trim_zeros(result, 'b') 

def multiply_polynomials(P, Q):  #P * Q
    np.trim_zeros(P, 'b')
    np.trim_zeros(Q, 'b')
    result = np.zeros(len(P) + len(Q) - 1)
    for i in xrange(len(P)):
        for j in xrange(len(Q)):
            result[i + j] += P[i] * Q[j]
    return np.trim_zeros(result, 'b')


##to-do
def pow_polynomial(P, n): #P^n
    np.trim_zeros(P, 'b')
    result = np.zeros(1)
    result[0] = 1
    for i in xrange(n):
        result = multiply_polynomials(result, P)
    return result

def get_max_degree(n, length): #get degree of n-variables polynomial up to length, ex. n = 3, length = 2 -> 1, n = 3, length = 5 -> 2
    if length == 1:
        return 0
    if n == 1:
        return length - 1
    max_degree = 1
    base_length = 1
    for degree in xrange(1, length):
        base_length =  (base_length * (n + degree)) / float(degree)
        base_length = int(base_length)
        if(length <= base_length):
            max_degree = degree
            break
    return max_degree

# #PROBLEM test 8 - przekroczony czas
def generate_base(n, max_degree, length): #get base of n-variables monomial up to max_degree, ex. n = 3, max_degree = 2 -> [[0,0,0],[1,0,0],[0,1,0],[0,0,1],[2,0,0],[1,1,0],[1,0,1],[0,2,0],[0,1,1],[0,0,2]]  
    base = []
    base.append(list(np.zeros(n, dtype = int)))
    if max_degree <= 2:
        for i in xrange(n):
            multi_index = np.zeros(n, dtype = int)
            multi_index[i] = 1
            base.append(list(multi_index))
            if len(base) >= length:
                return base
        if max_degree == 1:
            return base
    if max_degree == 2:
        for i in xrange(1, n + 1):
            multi_index = base[i]
            for j in xrange(i - 1, n):
                multi_index[j] += 1
                base.append(list(multi_index))
                multi_index[j] -= 1
                if len(base) >= length:
                    return base
        return base
 
    base = list(filter(lambda x: sum(x) <= max_degree, product(xrange(max_degree + 1), repeat = n))) 
    base.sort(key = sum, reverse = True) 
    base.reverse() 
    return base[:length]
    
def get_polynomial_value(P, x, base):  #get value of P(x) according to base
    result = 0
    n = len(base[0])
    for i in xrange (len(P)):
        x_prod = 1
        for j in xrange(n):
           x_prod *= x[j] ** base[i][j]
        result += P[i] * x_prod
    return result
    
def compute_polynomial_derivative(P, x, base):  ##jets?? to-do
    n = len(base[0])
    derivatives = np.zeros(n)
    for i in xrange(n):
        derivative = 0
        for j in xrange(len(P)):
            if base[j][i] != 0:
                x_prod = 1
                for k in xrange(n):
                    if k != i:
                        x_prod *= x[k] ** base[j][k]
                    else:
                        x_prod *= base[j][k] * x[k] ** (base[j][k] - 1) 
                derivative += P[j] * x_prod
        derivatives[i] = derivative   
    return derivatives
        
def compute_derivative(L, M, x, base):  #get derivative of L(x)/M(x) according to base
    n = len(base[0])
    Df = []
    for i in xrange(n):
            DL_i = compute_polynomial_derivative(L[i], x, base)
            DM_i = compute_polynomial_derivative(M[i], x, base)
            Lx_i = get_polynomial_value(L[i], x, base)
            Mx_i = get_polynomial_value(M[i], x, base)
            Df_i = (DL_i * Mx_i - DM_i * Lx_i) / (Mx_i ** 2)
            Df.append(Df_i)
    return Df

def get_eigenspace(Df): #todo
    vals, vects = np.linalg.eig(Df)
    min_index = np.argmin(np.abs(vals))
    positive_index = np.argmax(np.abs(vals) > 0)
    lambda_ = vals[min_index]
    u = vects[:, min_index]
    # u = u / np.linalg.norm(u) - niepotrzebne
    if u[positive_index] < 0:
        u *= -1
    return [lambda_, u]
    
    
    
#to-do!!!
def composition(P, J, base): #generate new polynomial of 1-variable by taking compostion P(J()) where P is n-variable polynomial and J is J0 + ... J_k-1
    n = len(base[0])
    k = len(J)
    
    deg = sum(base[-1]) * (k - 1)
    poly = np.zeros(deg)
    
    J_t = [] ###
    for i in xrange(n):
        v = np.zeros(deg)
        for j in xrange(k):
            v[j] = J[j][i]
        J_t.append(v)
    
    for i in xrange(len(P)):
        # v_prod = np.zeros(deg)
        v_prod = np.zeros(2)
        v_prod[0] = 1
        for j in xrange(n):
            v_prod = multiply_polynomials(v_prod, pow_polynomial(J_t[j], base[i][j]))
        poly = add_polynomials(poly, P[i] * v_prod)
    return np.trim_zeros(poly, 'b')
    
def divide_polynomials(L, M, k): #assuming that M[0] != 0
    if(M[0] == 0):
        index = 0
        while(M[index] == 0):
            index += 1
        return result[k + index]
    result = np.zeros(k + 1)
    result[0] = L[0] / M[0]
    for i in xrange(1, k + 1):
        if i < len(L):
            sum = L[i]
        else:
            sum = 0
        for j in xrange(1, min(i + 1, len(M))):
            sum -= result[i - j] * M[j]
        result[i] = sum / M[0]
    return result[k]

def print_output(output):
    n = len(output[0])
    for j in xrange(n):
        text = " ".join(["{:.17e}".format(output[i][j]) for i in xrange(len(output))])
        print(text)
    
    
#main
output = []
N = int(raw_input()) #number of taylor series coefficients
p = np.array(raw_input().split(), dtype = float) #fixed point
n = len(p) #dimension
L = [] #nominators
M = [] #denominators
max_length = 0 #max length of polynomials
for i in xrange(n): #polynomials
    L_i = np.array(raw_input().split(), dtype = float)
    M_i = np.array(raw_input().split(), dtype = float)
    np.trim_zeros(L_i, 'b')
    np.trim_zeros(M_i, 'b')
    L.append(L_i)
    M.append(M_i)
    max_length = max([max_length, len(L_i), len(M_i)])
max_degree = get_max_degree(n, max_length) #max degree of polynomials
base = generate_base(n, max_degree, max_length) #base of multi_indexes
Df = compute_derivative(L, M, p, base) #Df(p) 
lambda_, u = get_eigenspace(Df) #lambda_ and u satysfying task conditions
output.append(p)
output.append(u)

for k in xrange (2, N + 1):  #getting J_2, J_3, ..., J_N
    delta = np.zeros(n)
    lambda_Id = np.eye(n) * (lambda_ ** k) 
    for i in xrange(n):  
        nominator = composition(L[i], output, base)
        denominator = composition(M[i], output, base)
        delta[i] = -1 * divide_polynomials(nominator, denominator, k)    
    output_k = np.linalg.solve(np.subtract(Df, lambda_Id), delta) #equation (Df(p) - lambda_Id) * output_k = delta
    output.append(output_k)     

print_output(output)