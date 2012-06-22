package org.mybatisorm.test;

import java.lang.reflect.Field;
import java.util.Date;

import org.mybatisorm.annotation.Column;
import org.mybatisorm.annotation.ColumnNaming;
import org.mybatisorm.annotation.NamingRule;
import org.mybatisorm.annotation.Table;
import org.mybatisorm.annotation.handler.ColumnAnnotation;


@Table("NC_GROUP")
@ColumnNaming(NamingRule.LOWERCASE_UNDERSCORE)
public class Group implements java.io.Serializable {

	private static final long serialVersionUID = -6928722854068515088L;

	public static final int DELETED=-1;
	
	public static final int ACTIVE=1;	
	
	@Column(primaryKey = true, sequence="NC_GROUP_SEQ")
	protected Long groupID;
	
	@Column()
	protected Long parentGroupKey;
	
	@Column()
	protected Integer groupType;
		
	@Column()
	protected String groupId;
	
	@Column()
	protected String groupName;
	
	@Column()
	protected Long attachmentKey;
	
	@Column()
	protected String groupDescription;
	
	@Column(name="USER_COUNT")
	protected Integer userCount;
	
	@Column()
	protected Integer statusType;
	
	@Column()
	protected Long userKey;
	
	@Column()
	protected String userName;
	
	@Column
	protected Date registerDate;
	
	public Group(){}
	
	public static void main(String[] args) {
		for (Field field : Group.class.getDeclaredFields()) {
			if (field.isAnnotationPresent(Column.class))
				System.out.println(field.getName()+":"+ColumnAnnotation.getName(field));
		}
	}
	
}
