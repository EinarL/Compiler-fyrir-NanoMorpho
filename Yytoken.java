public class Yytoken
{
	public int number;
	public String string;
	public Yytoken( int t )
	{
		number = t;
	}
	public Yytoken( char c )
	{
		this((int)c);
	}
	public Yytoken( int t, String s )
	{
		number = t;
		string = s;
	}
	public String toString()
	{
		if( string != null ) return string;
		Character c = (char)number;
		return "\""+c+"\"";
	}
}
