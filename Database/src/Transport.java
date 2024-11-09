public class Transport {
	private String tnr;
	private String tdatum;
	private String tstart;
	private String tend;
	private String tvonort;
	private String tvonstrasse;
	private String tbisort;
	private String tbisstrasse;
	private TransportArt art;
	private String tbezugnr;
	private int tkmtotale;
	private int fnr;
	private String tsektionsort;

	// Enum für die Transportart
	public enum TransportArt {
		KANN_GEHEN, STUHL, LIEGE, KEIN_PATIENT, EIGENER_ROLLSTUHL
	}

	public Transport(String tnr, String tdatum, String tstart, String tend, String tvonort,
		String tvonstrasse, String tbisort, String tbisstrasse, TransportArt art,
		String tbezugnr, int tkmtotale, int fnr, String tsektionsort) {
		this.tnr = tnr;
		this.tdatum = tdatum;
		this.tstart = tstart;
		this.tend = tend;
		this.tvonort = tvonort;
		this.tvonstrasse = tvonstrasse;
		this.tbisort = tbisort;
		this.tbisstrasse = tbisstrasse;
		this.art = art;
		this.tbezugnr = tbezugnr;
		this.tkmtotale = tkmtotale;
		this.fnr = fnr;
		this.tsektionsort = tsektionsort;
	}

	public String generateInsertQuery() {
		return "INSERT INTO transport VALUES('" + tnr + "', '" + tdatum + "', '" + tstart + "', '"
			+ tend + "', '" + tvonort + "', '" + tvonstrasse + "', '" + tbisort + "', '" + tbisstrasse
			+ "', '" + art + "', " + (tbezugnr == null ? "NULL" : "'"+tbezugnr+"'") + ", " + tkmtotale + ", " + fnr + ", '" + tsektionsort + "')";
	}

	public String getTnr() {
		return tnr;
	}

	public String getTdatum() {
		return tdatum;
	}

	public String getTstart() {
		return tstart;
	}

	public String getTend() {
		return tend;
	}

	public String getTvonort() {
		return tvonort;
	}

	public String getTvonstrasse() {
		return tvonstrasse;
	}

	public String getTbisort() {
		return tbisort;
	}

	public String getTbisstrasse() {
		return tbisstrasse;
	}

	public TransportArt getArt() {
		return art;
	}

	public String getTbezugnr() {
		return tbezugnr;
	}

	public int getTkmtotale() {
		return tkmtotale;
	}

	public int getFnr() {
		return fnr;
	}

	public String getTsektionsort() {
		return tsektionsort;
	}
}
