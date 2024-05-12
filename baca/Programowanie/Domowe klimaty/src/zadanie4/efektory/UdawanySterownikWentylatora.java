package zadanie4.efektory;

public class UdawanySterownikWentylatora implements SterownikWentylatora {
	boolean on;
	@Override
	public void ustawWlaczenie(boolean wlaczony) {
		/*System.out.println("Wentylator włączony? " + on);*/
		on=wlaczony;

	}
	@Override
	public boolean jestWlaczony() {
		return on;
	}

}
