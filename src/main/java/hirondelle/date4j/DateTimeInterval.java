package hirondelle.date4j;

import hirondelle.date4j.DateTime.DayOverflow;
import hirondelle.date4j.DateTime.Unit;

/**
 Helper class for adding intervals of time. 
 The mental model of this class is similar to that of a car's odometer, except
 in reverse. 
 */
final class DateTimeInterval {

  /**  Constructor.  */
  DateTimeInterval(DateTime aFrom, DayOverflow aMonthOverflow){
	  fFrom = aFrom;
	    checkUnits();
	    fYear = fFrom.getYear() == null ? 1 : fFrom.getYear();
	    fMonth = fFrom.getMonth() == null ? 1 : fFrom.getMonth();
	    fDay = fFrom.getDay() == null ? 1 : fFrom.getDay();
	    fHour = fFrom.getHour() == null ? 0 : fFrom.getHour();
	    fMinute = fFrom.getMinute() == null ? 0 : fFrom.getMinute();
	    fSecond = fFrom.getSecond() == null ? 0 : fFrom.getSecond();
	    fNanosecond = fFrom.getNanoseconds() == null ? 0 : fFrom.getNanoseconds();
	    fDayOverflow = aMonthOverflow;
  }
  
  DateTimeInterval(DateTime aFrom, DayOverflow aMonthOverflow, int flag){
	  fFrom = aFrom;
	    checkUnits();
	    fYear = fFrom.getYear() == null ? 1 : fFrom.getYear();
	    fMonth = fFrom.getMonth() == null ? 1 : fFrom.getMonth();
	    fDay = fFrom.getDay() == null ? 1 : fFrom.getDay();
	    fHour = fFrom.getHour() == null ? 0 : fFrom.getHour();
	    fMinute = fFrom.getMinute() == null ? 0 : fFrom.getMinute();
	    fSecond = fFrom.getSecond() == null ? 0 : fFrom.getSecond();
	    
	    longFNanosecond = (long)(fFrom.getNanoseconds() == null ? 0 : fFrom.getNanoseconds());
	    fDayOverflow = aMonthOverflow;
  }
  
  DateTime plus(int aYear, int aMonth, int aDay, int aHour, int aMinute, int aSecond){
    return plusOrMinus(PLUS, aYear, aMonth, aDay, aHour, aMinute, aSecond);
  }
  
  DateTime minus(int aYear, int aMonth, int aDay, int aHour, int aMinute, int aSecond){
    return plusOrMinus(MINUS, aYear, aMonth, aDay, aHour, aMinute, aSecond);
  }
  
  DateTime minusPrecise(int aYear, int aMonth, int aDay, int aHour, int aMinute, int aSecond, int aNanoseconds){
	    return plusOrMinusPrecise(MINUS, aYear, aMonth, aDay, aHour, aMinute, aSecond, aNanoseconds);
	  }
  
  
  DateTime minusPreciseNano(int aYear, int aMonth, int aDay, int aHour, int aMinute, int aSecond, int aNanoseconds){
	    return plusOrMinusPreciseNano(MINUS, aYear, aMonth, aDay, aHour, aMinute, aSecond, aNanoseconds);
	  }
  
  DateTime plusPreciseNano(int aYear, int aMonth, int aDay, int aHour, int aMinute, int aSecond, int aNanoseconds){
	    return plusOrMinusPreciseNano(PLUS, aYear, aMonth, aDay, aHour, aMinute, aSecond, aNanoseconds);
	  }
  // PRIVATE 
  private final DateTime fFrom;
  private boolean fIsPlus;
  private DateTime.DayOverflow fDayOverflow;
  
  private int fYearIncr;
  private int fMonthIncr;
  private int  fDayIncr;
  private int fHourIncr;
  private int fMinuteIncr;
  private int fSecondIncr;
  private int fNanosecondIncr;
  private long longFNanosecondIncr;
  
  
  private Integer fYear;
  private Integer fMonth;
  private Integer fDay;
  private Integer fHour;
  private Integer fMinute;
  private Integer fSecond;
  private Integer fNanosecond;
  private long longFNanosecond;

  private static final int MIN = 0;
  private static final int MAX = 9999;
  private static final boolean PLUS = true;
  private static final boolean MINUS = false;

  private void checkUnits(){
    boolean success = false;
    if(fFrom.unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE, Unit.SECOND) ){
      success = true;
    }
    else if( fFrom.unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY) &&  fFrom.unitsAllAbsent(Unit.HOUR, Unit.MINUTE, Unit.SECOND) ){
      success = true;
    }
    else if ( fFrom.unitsAllAbsent(Unit.YEAR, Unit.MONTH, Unit.DAY) && fFrom.unitsAllPresent(Unit.HOUR, Unit.MINUTE, Unit.SECOND) ){
      success = true;
    }
    else {
      success = false;
    }
    if(! success ){
      throw new IllegalArgumentException("For interval calculations, DateTime must have year-month-day, or hour-minute-second, or both.");
    }
  }
  
  private DateTime plusOrMinus(boolean aIsPlus, Integer aYear, Integer aMonth, Integer aDay, Integer aHour, Integer aMinute, Integer aSecond){
    fIsPlus = aIsPlus;
    fYearIncr = aYear;
    fMonthIncr = aMonth;
    fDayIncr = aDay;
    fHourIncr = aHour;
    fMinuteIncr = aMinute;
    fSecondIncr = aSecond;
    
    checkRange(fYearIncr, "Year");
    checkRange(fMonthIncr, "Month");
    checkRange(fDayIncr, "Day");
    checkRange(fHourIncr, "Hour");
    checkRange(fMinuteIncr, "Minute");
    checkRange(fSecondIncr, "Second");
    
    changeYear();
    changeMonth();
    handleMonthOverflow();
    changeDay();
    changeHour();
    changeMinute();
    changeSecond();
    
    return new DateTime(fYear, fMonth, fDay, fHour, fMinute, fSecond, fFrom.getNanoseconds());
  }
  
  private DateTime plusOrMinusPrecise(boolean aIsPlus, Integer aYear, Integer aMonth, Integer aDay, Integer aHour, Integer aMinute, Integer aSecond, Integer aNanosecond){
	    fIsPlus = aIsPlus;
	    fYearIncr = aYear;
	    fMonthIncr = aMonth;
	    fDayIncr = aDay;
	    fHourIncr = aHour;
	    fMinuteIncr = aMinute;
	    fSecondIncr = aSecond;
	    fNanosecondIncr = aNanosecond;
	    
	    checkRange(fYearIncr, "Year");
	    checkRange(fMonthIncr, "Month");
	    checkRange(fDayIncr, "Day");
	    checkRange(fHourIncr, "Hour");
	    checkRange(fMinuteIncr, "Minute");
	    checkRange(fSecondIncr, "Second");
	    checkRange(fNanosecondIncr, "Nanosecond");
	    
	    
	    changeYear();
	    changeMonth();
	    handleMonthOverflow();
	    changeDay();
	    changeHour();
	    changeMinute();
	    changeSecond();
	    changeNanosecond();
	   
	    return new DateTime(fYear, fMonth, fDay, fHour, fMinute, fSecond, fNanosecond);
	  }
  
  private DateTime plusOrMinusPreciseNano(boolean aIsPlus, Integer aYear, Integer aMonth, Integer aDay, Integer aHour, Integer aMinute, Integer aSecond, long aNanosecond){
	    fIsPlus = aIsPlus;
	    fYearIncr = aYear;
	    fMonthIncr = aMonth;
	    fDayIncr = aDay;
	    fHourIncr = aHour;
	    fMinuteIncr = aMinute;
	    fSecondIncr = aSecond;
	    longFNanosecondIncr = aNanosecond;
	    
	    checkRange(fYearIncr, "Year");
	    checkRange(fMonthIncr, "Month");
	    checkRange(fDayIncr, "Day");
	    checkRange(fHourIncr, "Hour");
	    checkRange(fMinuteIncr, "Minute");
	    checkRange(fSecondIncr, "Second");
	    checkRange(fNanosecondIncr, "Nanosecond");
	    
	    
	    changeYear();
	    changeMonth();
	    handleMonthOverflow();
	    changeDay();
	    changeHour();
	    changeMinute();
	    changeSecond();
	    //changeNanosecond();
	    changeNanosecondMOD();
	    
	    //return new DateTime(fYear, fMonth, fDay, fHour, fMinute, fSecond, fNanosecond);
	    return new DateTime(fYear, fMonth, fDay, fHour, fMinute, fSecond,longFNanosecond,0);
	  }
  
  private void changeNanosecondMOD() 
  {
	 // while (count < fNanosecondIncr){
	      if(fIsPlus){
	        longFNanosecond = longFNanosecond + longFNanosecondIncr;        
	      }
	      else {
	    	  longFNanosecond = longFNanosecond - longFNanosecondIncr;        
	      }
	      if (longFNanosecond > 999999999){
	    	  longFNanosecond -= 1000000000L;
	        stepSecond();
	      }
	      else if (longFNanosecond < 0){
	    	  longFNanosecond += 1000000000L;
	        stepSecond();
	      }
	    
	   }


  private void checkRange(long aValue, String aName) {
	  if( aName.equals("Nanosecond"))
	  {
		  if( aValue < MIN || aValue > 999999999 )
			  throw new IllegalArgumentException(aName + " is not in the range " + MIN + ".." + MAX);
		  else
		  	return;
	  }  
	  if ( aValue <  MIN || aValue > MAX ) { 
		  throw new IllegalArgumentException(aName + " is not in the range " + MIN + ".." + MAX); 
	  }
  }
  
  private void changeYear(){
    if(fIsPlus){
      fYear = fYear + fYearIncr;
    }
    else {
      fYear = fFrom.getYear() - fYearIncr;
    }
    //the DateTime ctor will check the range of the year 
  }
  
  private void changeMonth(){
    int count = 0;
    while (count < fMonthIncr){
      stepMonth();
      count++;
    }
  }

  private void  changeDay(){
    int count = 0;
    while (count < fDayIncr){
      stepDay();
      count++;
    }
  }

  private  void changeHour(){
    int count = 0;
    while (count < fHourIncr){
      stepHour();
      count++;
    }
  }

  private void changeMinute(){
    int count = 0;
    while (count < fMinuteIncr){
      stepMinute();
      count++;
    }
  }
  
  private void changeSecond(){
    int count = 0;
    while (count < fSecondIncr){
      if(fIsPlus){
        fSecond = fSecond + 1;        
      }
      else {
        fSecond = fSecond - 1;        
      }
      if (fSecond > 59){
        fSecond = 0;
        stepMinute();
      }
      else if (fSecond < 0){
        fSecond = 59;
        stepMinute();
      }
      count++;
    }
  }
  
  private void changeNanosecond(){
	    int count = 0;
	    while (count < fNanosecondIncr){
	      if(fIsPlus){
	        fNanosecond = fNanosecond + 1;        
	      }
	      else {
	        fNanosecond = fNanosecond - 1;        
	      }
	      if (fNanosecond > 999999999){
	        fNanosecond = 0;
	        stepSecond();
	      }
	      else if (fNanosecond < 0){
	        fNanosecond = 999999999;
	        stepSecond();
	      }
	      count++;
	    }
	  }
  
  private void stepYear() {
    if(fIsPlus) {
      fYear = fYear + 1;
    }
    else {
      fYear = fYear - 1;
    }
  }
  
  private void stepMonth() {
    if(fIsPlus){
      fMonth = fMonth + 1;
    }
    else {
      fMonth = fMonth - 1;
    }
    if(fMonth > 12) { 
      fMonth = 1;
      stepYear();
    }
    else if(fMonth < 1){
      fMonth = 12;
      stepYear();
    }
  }

  private void stepDay() {
    if(fIsPlus){
      fDay = fDay + 1;
    }
    else {
      fDay = fDay - 1;
    }
    if(fDay > numDaysInMonth()){
      fDay = 1;
      stepMonth();
    }
    else if (fDay < 1){
      fDay = numDaysInPreviousMonth();
      stepMonth();
    }
  }
  
  private int numDaysInMonth(){
    return DateTime.getNumDaysInMonth(fYear, fMonth);
  }
  
  private int numDaysInPreviousMonth(){
    int result = 0;
    if(fMonth > 1) {
      result = DateTime.getNumDaysInMonth(fYear, fMonth - 1);
    }
    else {
      result = DateTime.getNumDaysInMonth(fYear - 1 , 12);
    }
    return result;
  }
  
  
  private void stepHour() {
    if(fIsPlus){
      fHour = fHour + 1;      
    }
    else {
      fHour = fHour - 1;      
    }
    if(fHour > 23){
      fHour = 0;
      stepDay();
    }
    else if (fHour < 0){
      fHour = 23;
      stepDay();
    }
  }
  
  private void stepMinute() {
    if(fIsPlus){
      fMinute = fMinute + 1;      
    }
    else {
      fMinute = fMinute - 1;      
    }
    if(fMinute > 59){
      fMinute = 0;
      stepHour();
    }
    else if (fMinute < 0){
      fMinute = 59;
      stepHour();
    }
  }
  
  private void stepSecond() {
	    if(fIsPlus){
	      fSecond = fSecond + 1;      
	    }
	    else {
	      fSecond = fSecond - 1;      
	    }
	    if(fSecond > 59){
	      fSecond = 0;
	      stepMinute();
	    }
	    else if (fSecond < 0){
	      fSecond = 59;
	      stepMinute();
	    }
	  }
  
  private void handleMonthOverflow(){
    int daysInMonth = numDaysInMonth();
    if( fDay > daysInMonth ){
      if(DayOverflow.Abort == fDayOverflow) {
        throw new RuntimeException(
          "Day Overflow: Year:" + fYear + " Month:" + fMonth + " has " + daysInMonth + " days, but day has value:" + fDay + 
          " To avoid these exceptions, please specify a different DayOverflow policy."
        );
      }
      else if (DayOverflow.FirstDay == fDayOverflow) {
        fDay = 1;
        stepMonth();
      }
      else if (DayOverflow.LastDay == fDayOverflow) {
        fDay = daysInMonth;
      }
      else if (DayOverflow.Spillover == fDayOverflow) {
        int overflowAmount = fDay - daysInMonth;
        fDay = overflowAmount;
        stepMonth();
      }
    }
  }

  /*
  public static double differenceBetweenDatesInSeconds(DateTime d1, DateTime d2)
  {
	  int [] datediff = difference(d1, d2);
	  //Datediff[7] - takes care of days, months and years 
	  return ( ( (double)datediff[6]/1000000000) + datediff[5] + 60*datediff[4] + 60*60*datediff[3] + 24*60*60*datediff[7]);
	  
	  
  }
  
  public static int [] difference(DateTime d1, DateTime d2){
	    
	    
	    int [] d1array = new int[7];
	    int [] d2array = new int[7];
	    int [] d3array = new int[8];
	    int [] months = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	    
	    
	    d1array[0] = d1.getYear();
	    d1array[1] = d1.getMonth();
	    d1array[2] = d1.getDay();
	    d1array[3] = d1.getHour();
	    d1array[4] = d1.getMinute();
	    d1array [5]= d1.getSecond();
	    d1array[6] = d1.getNanoseconds();
	    
	    d2array[0] = d2.getYear();
	    d2array[1] = d2.getMonth();
	    d2array[2] = d2.getDay();
	    d2array[3] = d2.getHour();
	    d2array[4] = d2.getMinute();
	    d2array [5]= d2.getSecond();
	    d2array[6] = d2.getNanoseconds();
	    
	    d3array[0] = d1array[0]-d2array[0];
	    d3array[1] = d1array[1]-d2array[1];
	    d3array[2] = d1array[2]-d2array[2];
	    d3array[3] = d1array[3]-d2array[3];
	    d3array[4] = d1array[4]-d2array[4];
	    d3array[5] = d1array[5]-d2array[5];
	    d3array[6] = d1array[6]-d2array[6];
	    
	  //Adding logic for leap year
	    
	    if(d2array[1] == 2)
	    {
	    	if( ((d2array[0] % 4) == 0 && (d2array[0] % 100 )!= 0) || ((d2array[0] % 400 ) == 0 ) )
	    			months[1]++;
	    	
	    }
	    
	    if(d3array[6] < 0)
	    {
	    	d3array[6]+=1000000000;
	    	d3array[5]--;
	    }
	    
	    if(d3array[5] < 0)
	    {
	    	d3array[5] += 60;
	    	d3array[4]--;
	    }
	    
	    if(d3array[4] < 0)
	    {
	    	d3array[4] += 60;
	    	d3array[3]--;
	    }
	    
	    if(d3array[3] < 0)
	    {
	    	d3array[3] += 24;
	    	d3array[2]--;
	    	
	    }
	    
	    if(d3array[2] < 0)
	    {
	    	d3array[2] += months[d2array[1]-1];
	    	d3array[1]--;
	    }
	    
	    if(d3array[1] < 0)
	    {
	    	d3array[1] += 12;
	    	d3array[0]--;
	    }
	    
	    d3array[7] = d3array[2];
	    //Change back to normal -> No Leap year
	    if ( months[1] == 29 )
	    	months[1] = 28;
	    
	    d1array[1]--;
	    
	    while ( d3array[1] > 0 )
	    {
	    	//Change back to normal -> No Leap year
		    if ( months[1] == 29 )
		    	months[1] = 28;
		    
	    	if(d1array[1] == 2 )
	    	{
	    		if( ((d1array[0] % 4) == 0 && (d1array[0] % 100 )!= 0) || ((d1array[0] % 400 ) == 0 ) )
	    			months[1]++;
	    	
	    	}
	    	d3array[7] += months[d1array[1]-1];
	    	
	    	d1array[1]--;
	    	
	    	
	    	if(d1array[1] == 0)
	    	{
	    		d1array[1]+=12;
	    		d1array[0] --;
	    	}
	    	
	    	if(d1array[1] == d2array[1] && d1array[0] == d2array[0] )
	    		break;
	    	
	    }
	    
	    
	   
	    
	    return d3array;
	  } */
  
}
