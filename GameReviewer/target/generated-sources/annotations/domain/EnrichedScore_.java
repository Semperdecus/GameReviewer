package domain;

import domain.QueryResult;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-03T19:39:53")
@StaticMetamodel(EnrichedScore.class)
public class EnrichedScore_ { 

    public static volatile SingularAttribute<EnrichedScore, Date> date;
    public static volatile SingularAttribute<EnrichedScore, Integer> score;
    public static volatile SingularAttribute<EnrichedScore, QueryResult> queryResult;

}