package edu.fsu.cs.contextprovider.data;

import net.smart_entity.AbstractEntity;
import net.smart_entity.AbstractField;
import net.smart_entity.BelongsTo;
import net.smart_entity.DateField;
import net.smart_entity.DoubleField;
import net.smart_entity.HasMany;
import net.smart_entity.IntegerField;
import net.smart_entity.StringField;
import net.smart_entity.TextField;

public class AccuracyEntity extends AbstractEntity {
	
    public final DateField Timestamp = new DateField(ContextConstants.CONTEXT_TIMESTAMP);
    public final IntegerField Place = new IntegerField(ContextConstants.DERIVED_PLACE);
    public final IntegerField Movement = new IntegerField(ContextConstants.MOVEMENT_STATE);
    public final IntegerField Activity = new IntegerField(ContextConstants.DERIVED_ACTIVITY);
    public final IntegerField Shelter = new IntegerField(ContextConstants.DERIVED_SHELTER);
    public final IntegerField OnPerson = new IntegerField(ContextConstants.DERIVED_ONPERSON);

	@Override
	public AbstractEntity createNewInstance() {
		// TODO Auto-generated method stub
		return new AccuracyEntity();
	}

	@Override
	public BelongsTo[] getBelongsToRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractField<?>[] getFields() {
		// TODO Auto-generated method stub
		return new AbstractField<?>[]{ Timestamp, Place, Movement, Activity, Shelter, OnPerson };
	}

	@Override
	public HasMany[] getHasManyRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchemaName() {
		// TODO Auto-generated method stub
		return "DerivedEntity";
	}

}
