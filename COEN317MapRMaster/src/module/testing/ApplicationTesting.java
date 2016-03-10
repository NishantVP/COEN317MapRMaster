/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.testing;

public class ApplicationTesting {

	public static void main(String[]args){
		ShufflerJob.combineStreams("alphabet=1,google=1,this=1,this=1,aim=1,om=1,sneha=1,the=1,this=1,google=1,google=1,beta=1,");
		ShufflerJob.combineStreams("animal=1,zoo=1,this=1,that=1,yoyo=1,octocat=1,octopus=1,zebra=1,octocat=1,");
		ShufflerJob.combineStreams("aim=1,sneha=1,nishant=1,the=1,that=1,");
		
		ReducerJob.reduceAndroid(ShufflerJob.getShufflerOutput());
		
	}
}