import java.io.Serializable;

public class Strecken implements Serializable {

	private int snr;
	private String tnr;
	private String svonort;
	private String sbisort;
	private int sstuhl;
	private int sgehend;
	private int ssitz;
	private boolean sliege;
	private Transport transport;

	public Strecken() {}

	public Strecken(String tnr, String svonort, String sbisort, int sstuhl, int sgehend, int ssitz, boolean sliege) {
		this.tnr = tnr;
		this.svonort = svonort;
		this.sbisort = sbisort;
		this.sstuhl = sstuhl;
		this.sgehend = sgehend;
		this.ssitz = ssitz;
		this.sliege = sliege;
	}

	public int getSnr() {
		return snr;
	}

	public void setSnr(int snr) {
		this.snr = snr;
	}

	public String getTnr() {
		return tnr;
	}

	public void setTnr(String tnr) {
		this.tnr = tnr;
	}

	public String getSvonort() {
		return svonort;
	}

	public void setSvonort(String svonort) {
		this.svonort = svonort;
	}

	public String getSbisort() {
		return sbisort;
	}

	public void setSbisort(String sbisort) {
		this.sbisort = sbisort;
	}

	public int getSstuhl() {
		return sstuhl;
	}

	public void setSstuhl(int sstuhl) {
		this.sstuhl = sstuhl;
	}

	public int getSgehend() {
		return sgehend;
	}

	public void setSgehend(int sgehend) {
		this.sgehend = sgehend;
	}

	public int getSsitz() {
		return ssitz;
	}

	public void setSsitz(int ssitz) {
		this.ssitz = ssitz;
	}

	public boolean isSliege() {
		return sliege;
	}

	public void setSliege(boolean sliege) {
		this.sliege = sliege;
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

	public String getInsertQuery() {
		return String.format(
			"INSERT INTO strecken (tnr, svonort, sbisort, sstuhl, sgehend, ssitz, sliege) "
				+ "VALUES ('%s', '%s', '%s', %d, %d, %d, %b);",
			tnr, svonort, sbisort, sstuhl, sgehend, ssitz, sliege
		);
	}
}
