package hu.swicon.oss.hibernate.dialect;

import org.hibernate.dialect.Oracle10gDialect;

public class Oracle10gExtendedDialect extends Oracle10gDialect {

	public Oracle10gExtendedDialect() {
		
		super();
		System.out.println("lonyalo");
		System.out.println(org.hibernate.Version.getVersionString());

    }
	
	public String getLimitString(String query, int offset, int limit) {
		System.out.println("lonyalo2");
		return getLimitString( query, ( offset > 0 || forceLimitUsage() )  );
	}
	
	
	
	@Override
	public String getLimitString(String sql, boolean hasOffset) {
		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		final int forUpdateIndex = sql.toLowerCase().lastIndexOf( "for update") ;
		if ( forUpdateIndex > -1 ) {
			// save 'for update ...' and then remove it
			forUpdateClause = sql.substring( forUpdateIndex );
			sql = sql.substring( 0, forUpdateIndex-1 );
			isForUpdate = true;
		}
		
		final StringBuilder pagingSelect = new StringBuilder( sql.length() + 100 );
		//if (hasOffset) {
			pagingSelect.append( "select /*+ FIRST_ROWS */* from ( select row_.*, row_number() over (order by null) rownum_ from ( " );
		//}
		//else {
		//	pagingSelect.append( "select * from ( " );
		//}
		pagingSelect.append( sql );
		if (hasOffset) {
			pagingSelect.append( " ) row_ ) where rownum_ between ? +1 and ?" );
		}
		else {
			pagingSelect.append( " ) row_ ) where rownum_ between 1 and ?" );
		}
		
		if ( isForUpdate ) {
			pagingSelect.append( " " );
			pagingSelect.append( forUpdateClause );
		}

		return pagingSelect.toString();
	}
	

	
}
