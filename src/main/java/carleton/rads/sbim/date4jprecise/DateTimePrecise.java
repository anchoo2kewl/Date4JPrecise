package carleton.rads.sbim.date4jprecise;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class DateTimePrecise{
	
	public static void main(String [] args)
	{
		DateTime dt1 = new DateTime(args[0]);
		DateTime dt2 = new DateTime(args[1]);
		
		System.out.println(dt2.testNano());
		DateTime test = DateTime.now(TimeZone.getDefault());
		System.out.println(test);
		System.out.println("milli:->"+DateTime.forInstant(System.currentTimeMillis(),TimeZone.getDefault()));
		long startTime = System.nanoTime(); 
		
		System.out.println("nano:->"+startTime);
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("nano:->"+estimatedTime);
		
		if(dt1.lt(dt2))
		{
			System.out.println("Passed2!");
		}
	}

}
