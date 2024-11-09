import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import javax.swing.Icon;

public class CustomWaypoint extends DefaultWaypoint {
    private final Icon icon;

    public CustomWaypoint(GeoPosition position, Icon icon) {
        super(position);
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }
}
