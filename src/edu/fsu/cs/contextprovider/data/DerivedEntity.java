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

public class DerivedEntity extends AbstractEntity {
	
    public final TextField Place = new TextField(ContextConstants.DERIVED_PLACE);
    public final TextField Activity = new TextField(ContextConstants.DERIVED_ACTIVITY);
    public final TextField Shelter = new TextField(ContextConstants.DERIVED_SHELTER);
    public final TextField Pocket = new TextField(ContextConstants.DERIVED_ONPERSON);
    public final TextField Mood = new TextField(ContextConstants.DERIVED_MOOD);

	@Override
	public AbstractEntity createNewInstance() {
		// TODO Auto-generated method stub
		return new DerivedEntity();
	}

	@Override
	public BelongsTo[] getBelongsToRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractField<?>[] getFields() {
		// TODO Auto-generated method stub
		return new AbstractField<?>[]{ Place, Activity, Shelter, Pocket, Mood };
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
