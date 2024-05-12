package zadanie4.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import zadanie4.czas.PoraDoby;
import zadanie4.efektory.SterownikKotla;
import zadanie4.efektory.SterownikWentylatora;
import zadanie4.efektory.UdawanySterownikKotla;
import zadanie4.efektory.UdawanySterownikWentylatora;
import zadanie4.ogrzewanie.NiejednoznaczaTemperatura;
import zadanie4.ogrzewanie.StalotemperaturoweSterowanieOgrzewaniem;
import zadanie4.ogrzewanie.ZmiennotemperaturoweSterowanieOgrzewaniem;
import zadanie4.sensory.SensorNiedostepny;
import zadanie4.wentylacja.CzasoweSterowanieWentylacja;
import zadanie4.wentylacja.WilgotnoscioweSterowanieWentylacja;

public class TestyJawne {
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int MILLIS_IN_MINUTE = 1000 * SECONDS_IN_MINUTE;
    TestowyTermometr termZewn = new TestowyTermometr();
    TestowyTermometr termWewn = new TestowyTermometr();
    TestowyZegar zegar = new TestowyZegar();
    SterownikKotla kociol = new UdawanySterownikKotla();
    TestowyHigrometr higr = new TestowyHigrometr();
    SterownikWentylatora wentylator = new UdawanySterownikWentylatora();
    private static void assertTruth(boolean assertion, String message){
		if (!assertion){
			throw new AssertionError(message);
		}
    }
    private void testujTemperature(double tempZewn, double tempWewn, boolean on, double temperature) {
	termWewn.ustawTemperature(tempWewn);
	termZewn.ustawTemperature(tempZewn);

	if (!on) {
	    assertTruth(kociol.pobierzZadanaTemperature() < kociol.pobierzMinimalnaTemperatureWlaczenia(),
		    "Kociol nieslusznie  wlaczony");
	} else {
	    assertTruth(
		    Math.abs(kociol.pobierzZadanaTemperature() - temperature) <= 1,
		    "Niewlasciwa temperatura kotla, roznica: "
			    + (Math.abs(kociol.pobierzZadanaTemperature() - temperature)));

	}

    }

    private void testujPrzedzialZTemperatura(long startTime, int fromMin, int toMin, double tempZewn, double tempWewn,
	    boolean on, double temperature) {
	for (int i = fromMin; i < toMin; i++) {
	    zegar.ustawCzas(new Date(startTime + MILLIS_IN_MINUTE * i));
	    termWewn.ustawTemperature(tempWewn);
	    termZewn.ustawTemperature(tempZewn);
	    if (!on) {
		assertTruth(kociol.pobierzZadanaTemperature() < kociol.pobierzMinimalnaTemperatureWlaczenia(),
			"Kociol nieslusznie  wlaczony");
	    } else {
		assertTruth(
			Math.abs(kociol.pobierzZadanaTemperature() - temperature) <= 1,
			"Niewlasciwa temperatura kotla, roznica: "
				+ (Math.abs(kociol.pobierzZadanaTemperature() - temperature)));
	    }

	}

    }
    private void testujPrzedzialZWilgotnoscia(long startTime, int fromMin, int toMin, double higro, boolean on) {
	for (int i = fromMin; i < toMin; i++) {

	    zegar.ustawCzas(new Date(startTime + MILLIS_IN_MINUTE * i));
	    higr.ustawWilgotnosc(higro);
	    assertTruth(!(on ^ wentylator.jestWlaczony()), "Wentylator nieslusznie "
		    + (on ? " wylaczony " : "wlaczony"));



	     System.out.print(zegar.pobierzCzas()); System.out.print(" " +
	     wentylator.jestWlaczony()); System.out.println();



	}

    }

    private void testujPrzedzial(long startTime, int fromMin, int toMin, boolean on) {
	for (int i = fromMin; i < toMin; i++) {
	    zegar.ustawCzas(new Date(startTime + MILLIS_IN_MINUTE * i));
	    assertTruth(!(on ^ wentylator.jestWlaczony()), "Wentylator nieslusznie "
		    + (on ? " wylaczony " : "wlaczony"));
	}

    }

    public void testujStalotemperaturowe() {
	StalotemperaturoweSterowanieOgrzewaniem sso = utworzStalotemperaturowe();

	sso.ustawDocelowaTemperatura(20);
	sso.ustawMinimalnaRoznica(10);
	sso.ustawWspolczynnik(1.6);
	testujTemperature(3, 20, true, 51.2);
	testujTemperature(-10, 20, true, 72);
	testujTemperature(-9, 22, true, 70.4);

	testujTemperature(12, 19, true, 41.6);
	testujTemperature(14, 15, true, 48);

	testujTemperature(4, 15, true, 48);

	//testujTemperature(9, 15, true, 48);
	testujTemperature(14, 20, false, 00);

    }

    public void testujZmiennotemperaturowe() throws NiejednoznaczaTemperatura {
	ZmiennotemperaturoweSterowanieOgrzewaniem sso = utworzZmiennotemperaturowe();

	GregorianCalendar cal = new GregorianCalendar();
	cal.clear();
	cal.set(2012, 3, 1);
	cal.set(Calendar.HOUR_OF_DAY, 13);
	sso.ustawDocelowaTemperatura(20);
	sso.ustawMinimalnaRoznica(10);
	sso.ustawWspolczynnik(1.6);
	long sT = cal.getTime().getTime();

	sso.dodajOkresZezmienionaTemperatura(new PoraDoby(22 * SECONDS_IN_HOUR + 45 * SECONDS_IN_MINUTE, 6
		* SECONDS_IN_HOUR + 15 * SECONDS_IN_MINUTE), 18);
	sso.dodajOkresZezmienionaTemperatura(new PoraDoby(17 * SECONDS_IN_HOUR + 10 * SECONDS_IN_MINUTE, 20
		* SECONDS_IN_HOUR + 20 * SECONDS_IN_MINUTE), 22);

	testujPrzedzialZTemperatura(sT, 0, 120, 7, 20, true, 44.8);
	testujPrzedzialZTemperatura(sT, 120, 240, 11, 19, true, 41.6);
	testujPrzedzialZTemperatura(sT, 240, 250, 11, 20, false, 00);

	testujPrzedzialZTemperatura(sT + 250 * MILLIS_IN_MINUTE, 0, SECONDS_IN_MINUTE, 11, 20, true, 41.6);
	testujPrzedzialZTemperatura(sT + 250 * MILLIS_IN_MINUTE, SECONDS_IN_MINUTE, 80, 12.5, 22, false, 00);
	testujPrzedzialZTemperatura(sT + 250 * MILLIS_IN_MINUTE, 80, 190, 7, 22, true, 48);

	testujPrzedzialZTemperatura(sT + 440 * MILLIS_IN_MINUTE, 0, 145, 8.5, 21, true, 42.4);

	testujPrzedzialZTemperatura(sT + 585 * MILLIS_IN_MINUTE, 0, SECONDS_IN_MINUTE, 8.5, 19, false, 00);
	testujPrzedzialZTemperatura(sT + 585 * MILLIS_IN_MINUTE, SECONDS_IN_MINUTE, 80, 8.5, 16.5, true, 42.4);
	testujPrzedzialZTemperatura(sT + 585 * MILLIS_IN_MINUTE, 80, 190, 4, 18, true, 46.4);

    }

    public void testujWilgotnosciowe() {
	WilgotnoscioweSterowanieWentylacja wsw = utworzWilgotnosciowe();
	wsw.ustawMaksymalnaPozadanaWilgotnosc(SECONDS_IN_MINUTE);
	GregorianCalendar cal = new GregorianCalendar();
	cal.clear();
	cal.set(2012, 3, 1);
	cal.set(Calendar.HOUR, 23);
	cal.set(Calendar.MINUTE, 25);
	wentylator.ustawWlaczenie(false);
	long sT = cal.getTime().getTime();
	int i = 0;
	testujPrzedzialZWilgotnoscia(sT, i, i += SECONDS_IN_MINUTE, 50, true);
	testujPrzedzialZWilgotnoscia(sT, i, i += SECONDS_IN_MINUTE, 50, false);
	testujPrzedzialZWilgotnoscia(sT, i, i += 10, 62, true);
	for (int j = 3; j < 7; j += 4) {
	    testujPrzedzialZWilgotnoscia(sT, i, i += 15, 50, true);
	    testujPrzedzialZWilgotnoscia(sT, i, i += SECONDS_IN_MINUTE, 50, false);
	    testujPrzedzialZWilgotnoscia(sT, i, i += 45, 50, true);
	    testujPrzedzialZWilgotnoscia(sT, i, i += SECONDS_IN_MINUTE, 50, false);
	}
	testujPrzedzialZWilgotnoscia(sT, i, i += 15, 50, true);
	testujPrzedzialZWilgotnoscia(sT, i, i += SECONDS_IN_MINUTE, 50, false);
	testujPrzedzialZWilgotnoscia(sT, i, i += 45, 50, true);
	testujPrzedzialZWilgotnoscia(sT, i, i += 55, 50, false);
	testujPrzedzialZWilgotnoscia(sT, i, i += 35, 65, true);
	testujPrzedzialZWilgotnoscia(sT, i, i += 15, 50, true);
	testujPrzedzialZWilgotnoscia(sT, i, i += 65, 50, false);
	for (int j = 14; j < 22; j += 4) {
	    testujPrzedzialZWilgotnoscia(sT, i, i += 15, 50, true);
	    testujPrzedzialZWilgotnoscia(sT, i, i += 55, 50, false);
	    testujPrzedzialZWilgotnoscia(sT, i, i += 45, 50, true);
	    testujPrzedzialZWilgotnoscia(sT, i, i += 65, 50, false);
	}

    }

    public void testujCzasowe() {
	CzasoweSterowanieWentylacja csw = utworzCzasowe();
	GregorianCalendar cal = new GregorianCalendar();
	cal.clear();
	cal.set(2012, 3, 1);
	cal.set(Calendar.HOUR_OF_DAY, 14);
	long sT = cal.getTime().getTime();
	wentylator.ustawWlaczenie(false);

	csw.dodajOkresWlaczenia(new PoraDoby(17 * SECONDS_IN_HOUR, 19 * SECONDS_IN_HOUR));
	csw.dodajOkresWlaczenia(new PoraDoby(23 * SECONDS_IN_HOUR, 6 * SECONDS_IN_HOUR));
	csw.dodajOkresWlaczenia(new PoraDoby(10 * SECONDS_IN_HOUR, 12 * SECONDS_IN_HOUR));
	int i = 0;
	testujPrzedzial(sT, i + 0, i + 180, false);
	testujPrzedzial(sT, i + 180, i + 300, true);
	testujPrzedzial(sT, i + 300, i + 540, false);
	testujPrzedzial(sT, i + 540, i + 960, true);
	testujPrzedzial(sT, i + 960, i + 1200, false);
	testujPrzedzial(sT, i + 1200, i + 1320, true);
	testujPrzedzial(sT, i + 1320, i + 1620, false);
	testujPrzedzial(sT, i + 1320, i + 1620, false);

    }

    private ZmiennotemperaturoweSterowanieOgrzewaniem utworzZmiennotemperaturowe() {
	kociol.ustawZadanaTemperature(kociol.pobierzMinimalnaTemperatureWlaczenia() - 1);

	termWewn.wyczyscObserwatorowSensorow();
	termZewn.wyczyscObserwatorowSensorow();

	zegar.wyczyscObserwatorowSensorow();

	ZmiennotemperaturoweSterowanieOgrzewaniem csw = new ZmiennotemperaturoweSterowanieOgrzewaniem();

	csw.ustawSterownikPieca(kociol);
	csw.ustawTermometrWewnetrzny(termWewn);
	termWewn.dodajObserwartoraSensorow(csw);
	termZewn.dodajObserwartoraSensorow(csw);
	zegar.dodajObserwartoraSensorow(csw);
	csw.ustawTermometrZewnetrzny(termZewn);
	csw.ustawZegar(zegar);

	return csw;
    }

    private StalotemperaturoweSterowanieOgrzewaniem utworzStalotemperaturowe() {
	kociol.ustawZadanaTemperature(kociol.pobierzMinimalnaTemperatureWlaczenia() - 1);
	termWewn.wyczyscObserwatorowSensorow();
	termZewn.wyczyscObserwatorowSensorow();
	StalotemperaturoweSterowanieOgrzewaniem csw = new StalotemperaturoweSterowanieOgrzewaniem();

	csw.ustawSterownikPieca(kociol);
	csw.ustawTermometrWewnetrzny(termWewn);
	termWewn.dodajObserwartoraSensorow(csw);
	termZewn.dodajObserwartoraSensorow(csw);
	csw.ustawTermometrZewnetrzny(termZewn);

	return csw;
    }

    private WilgotnoscioweSterowanieWentylacja utworzWilgotnosciowe() {
	zegar.wyczyscObserwatorowSensorow();
	higr.wyczyscObserwatorowSensorow();

	WilgotnoscioweSterowanieWentylacja wsw = new WilgotnoscioweSterowanieWentylacja();
	wsw.ustawHigrometr(higr);
	higr.dodajObserwartoraSensorow(wsw);
	wsw.ustawZegar(zegar);
	zegar.dodajObserwartoraSensorow(wsw);
	wsw.ustawSterownikWentylatora(wentylator);

	wentylator.ustawWlaczenie(false);
	return wsw;
    }

    protected CzasoweSterowanieWentylacja utworzCzasowe() {
	zegar.wyczyscObserwatorowSensorow();

	CzasoweSterowanieWentylacja csw = new CzasoweSterowanieWentylacja();
	csw.ustawZegar(zegar);
	zegar.dodajObserwartoraSensorow(csw);
	csw.ustawSterownikWentylatora(wentylator);

	wentylator.ustawWlaczenie(false);
	return csw;
    }
    public static void main(String [] args){
	TestyJawne t= new TestyJawne();
	try {
	    t.testujZmiennotemperaturowe();
	} catch (NiejednoznaczaTemperatura e) {
	   System.out.println("Ten wyjatek nie powinien zostac rzucony w tej sytuacji" + e.getMessage());
	}
	t.testujStalotemperaturowe();

	t.testujCzasowe();
	t.testujWilgotnosciowe();
    }
}
