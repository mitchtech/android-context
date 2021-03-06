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

public class SystemEntity extends AbstractEntity {
	
    public final StringField State = new StringField(ContextConstants.SYSTEM_STATE);
    public final IntegerField BatteryLevel = new IntegerField(ContextConstants.SYSTEM_BATTERY_LEVEL);
    public final TextField Plugged = new TextField(ContextConstants.SYSTEM_PLUGGED);
    public final DateField LastPlugged = new DateField(ContextConstants.SYSTEM_LAST_PLUGGED);
    public final DateField LastPresent = new DateField(ContextConstants.SYSTEM_LAST_PRESENT);
    public final TextField SSID = new TextField(ContextConstants.SYSTEM_WIFI_SSID);
    public final IntegerField Signal = new IntegerField(ContextConstants.SYSTEM_WIFI_SIGNAL);

    @Override
	public AbstractEntity createNewInstance() {
		// TODO Auto-generated method stub
		return new SystemEntity();
	}

	@Override
	public BelongsTo[] getBelongsToRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractField<?>[] getFields() {
		// TODO Auto-generated method stub
		return new AbstractField<?>[]{ State, BatteryLevel, Plugged, LastPlugged, LastPresent, SSID, Signal };
	}

	@Override
	public HasMany[] getHasManyRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchemaName() {
		// TODO Auto-generated method stub
		return "SystemEntity";
	}

}
