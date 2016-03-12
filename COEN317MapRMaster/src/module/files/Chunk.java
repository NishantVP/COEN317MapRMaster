/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.files;


public class Chunk {
	
	private String chunkFilePathName;
	private boolean sent;
	private boolean processed;
	private int chunkNumber;
	
	public Chunk(String path, int number) {
		this.chunkFilePathName = path;
		sent = false;
		processed = false;
		this.chunkNumber = number;
	}
	
	public int getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(int chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	public String getChunkFilePathName() {
		return chunkFilePathName;
	}

	public void setChunkFilePathName(String chunkFilePathName) {
		this.chunkFilePathName = chunkFilePathName;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
}
