package net.starype.colorparkour.entity.player;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import net.starype.colorparkour.core.ColorParkourMain;

/**
 * @author Askigh
 *
 * Sets up a basic camera replacing {@link com.jme3.input.FlyByCamera}
 * incapsulated into a Player object. Manages camera rotation
 *
 * The classes whose names are similar or equal to other classes end with 'SY', in order not to mix up
 * All of them are provided by Starype
 */
public class CameraSY implements AnalogListener {

    private Camera source;
    private Vector3f initialUp;
    private InputManager listener;
    private ColorParkourMain main;

    private static final int SENSITIVITY = 600;
    private static final String[] MAPPINGS = {"left", "right", "top", "bottom"};

    protected CameraSY(ColorParkourMain main, Camera source) {
        this.main = main;
        this.source = source;
        initialUp = source.getUp().clone();
        listener = main.getInputManager();
    }

    protected void initMappings() {

        listener.addMapping("left", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        listener.addMapping("right", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        listener.addMapping("top", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        listener.addMapping("bottom", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        listener.addListener(this, MAPPINGS);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

        if(main.getPlayerInventory().isGuiActive()) {
            return;
        }
        switch (name) {
            case "left":
                rotateCamera(value * tpf, initialUp);
                break;
            case "right":
                rotateCamera(-value * tpf, initialUp);
                break;
            case "top":
                rotateCamera(-value * tpf, source.getLeft());
                break;
            case "bottom":
                rotateCamera(value * tpf, source.getLeft());
                break;
        }
    }

    // Code from com.jme3.input.FlyByCamera
    private void rotateCamera(float value, Vector3f axis) {

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(SENSITIVITY * value, axis);

        Vector3f up = source.getUp();
        Vector3f left = source.getLeft();
        Vector3f dir = source.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        source.setAxes(q);
    }
    public Camera getSource() { return source; }
}
