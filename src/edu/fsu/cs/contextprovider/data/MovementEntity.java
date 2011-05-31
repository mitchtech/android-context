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

public class MovementEntity extends AbstractEntity {
	
    public final DateField Timestamp = new DateField(ContextConstants.CONTEXT_TIMESTAMP);
    public final TextField State = new TextField(ContextConstants.MOVEMENT_STATE);
    public final DoubleField Speed = new DoubleField(ContextConstants.MOVEMENT_SPEED);
    public final DoubleField Bearing = new DoubleField(ContextConstants.MOVEMENT_BEARING);
    public final IntegerField Steps = new IntegerField(ContextConstants.MOVEMENT_STEP_COUNT);
    public final DateField LastStep = new DateField(ContextConstants.MOVEMENT_LAST_STEP);
    public final TextField Accuracy = new TextField(ContextConstants.CONTEXT_ACCURACY);

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
