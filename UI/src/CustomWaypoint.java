import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import javax.swing.Icon;

public class CustomWaypoint extends DefaultWaypoint {
    private final Icon icon;
    private final CustomWaypointType type;

    public CustomWaypoint(GeoPosition position, Icon icon, CustomWaypointType type) {
        super(position);
        this.icon = icon;
        this.type = type;
    }

    public Icon getIcon() {
        return icon;
    }

    public CustomWaypointType getType() {
        return type;
    }
}