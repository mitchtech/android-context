package edu.fsu.cs.contextprovider.data;

import edu.fsu.cs.contextprovider.ContextConstants;
import net.smart_entity.AbstractEntity;
import net.smart_entity.AbstractField;
import net.smart_entity.BelongsTo;
import net.smart_entity.DateField;
import net.smart_entity.DoubleField;
import net.smart_entity.HasMany;
import net.smart_entity.IntegerField;
import net.smart_entity.StringField;
import net.smart_entity.TextField;

public class Social extends AbstractEntity {
	
    public final DateField Timestamp = new DateField(ContextConstants.CONTEXT_TIMESTAMP);
    public final TextField Contact = new TextField(ContextConstants.SOCIAL_CONTACT);
    public final TextField Communication = new TextField(ContextConstants.SOCIAL_COMMUNICATION);
    public final StringField Message = new StringField(ContextConstants.SOCIAL_MESSAGE);
    public final DateField LastIncoming = new DateField(ContextConstants.SOCIAL_LAST_OUT);
    public final DateField LastOutgoing = new DateField(ContextConstants.SOCIAL_LAST_IN);

	@Override
	public AbstractEntity createNewInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BelongsTo[] getBelongsToRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractField<?>[] getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasMany[] getHasManyRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchemaName() {
		// TODO Auto-generated method stub
		return null;
	}

}
