package io.github.cakilgan.cgraphics.c2d.render.debug;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cmath.CMath;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CESceneComponent;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import org.jbox2d.common.Vec2;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static org.joml.Math.toRadians;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class C2DDebugRenderer implements CESceneComponent {
    private  int MAX_LINES = 100000;

    boolean shouldRender = true;

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    boolean shouldDebugRenderJPhysics = false;

    public void setShouldDebugRenderJPhysics(boolean shouldDebugRenderJPhysics) {
        this.shouldDebugRenderJPhysics = shouldDebugRenderJPhysics;
    }
    public boolean isShouldDebugRenderJPhysics() {
        return shouldDebugRenderJPhysics;
    }

    private  List<C2DLine> lines = new ArrayList<>();

    public  List<C2DLine> getLines() {
        return lines;
    }
    CEScene scene;
    public C2DDebugRenderer(CEScene scene){
        this.scene = scene;
    }
    // 6 floats per vertex, 2 vertices per line
    private  float[] vertexArray = new float[MAX_LINES * 7 * 2];
    private  CGShader shader = CEngine.BATCH_SHADER;

    private  int vaoID;
    private  int vboID;

    private  boolean started = false;

    public  void draw(int z){
        for (C2DLine line:
                lines) {

        }
    }
    public  void start() {
        // Generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create the vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public void flush(){
        lines = new ArrayList<>();
    }
    public  void beginFrame() {
        lines = new ArrayList<>();
        if (shouldRender){
            scene.objectSystem().getObjects().forEach(new BiConsumer<CEObjectID, CEObject>() {
                @Override
                public void accept(CEObjectID ceObjectID, CEObject object) {
                    final boolean[] tr = {false,false};
                    final Vector3f[] colorcode = new Vector3f[1];
                    final float[] zpos = new float[]{0f};
                    object.getComponents().forEach(new BiConsumer<String, CEOComponent>() {
                        @Override
                        public void accept(String s, CEOComponent ceoComponent) {
                            if (ceoComponent instanceof C2DDebugDraw){
                                tr[0] =true;
                                colorcode[0] = ((C2DDebugDraw) ceoComponent).getColor();
                                zpos[0] = ((C2DDebugDraw) ceoComponent).getZPos();
                            }
                        }
                    });
                    if (tr[0]){
                        //addCircle(object.getTransform().getPos(),1f,new Vector3f(1,0,0));
                        addBox2D(object.getTransform().getPos(),object.getTransform().getScale(),object.getTransform().getRotation(),colorcode[0],zpos[0]);
                    }
                }
            });
        }
        if (isShouldDebugRenderJPhysics()) {
            for (Body body : scene.getWorld().getWorld().bodies) {
                if (body.shape instanceof Polygon polygon){
                    addBox2D(new Vector2f((float) body.position.x, (float) body.position.y),new Vector2f((float) -polygon.vertices[0].x*2f, (float) -polygon.vertices[0].y*2f), org.joml.Math.toDegrees((float) body.orientation),new Vector3f(1,0,0));
                } else if (body.shape instanceof Circle circle) {
                    addCircle(new Vector2f((float) -body.position.x, (float) body.position.y), (float) circle.radius,new Vector3f(1,0,0));
                }
            }
        }


        if (!started) {
            start();
            started = true;
        }

    }

    public Vector2f to(Vec2 from){
        return new Vector2f(from.x,from.y);
    }

    public Vec2 to(Vector2f from){
      return new Vec2(from.x,from.y);
    }
    public void removeLine2D(int index){
        lines.remove(index);
        lineCount--;
    }

    public  void draw() {
        if (lines.size() <= 0){
            return;
        }
        vertexArray = new float[MAX_LINES*7*2];
        int index = 0;
        for (C2DLine line : lines) {
            for (int i=0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();
                float zpos = line.getzPos();

                // Load position
                vertexArray[index] = position.x;//0 7
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = zpos;

                // Load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                vertexArray[index + 6] = 1f;
                index += 7;
            }
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size()*7*2));

        // Use our shader
        shader.start();
        shader._mat4f("uProjection", ((CEScene) CEngine.SCENE.getCurrentScene()).getCamera().getProjectionMatrix());
        shader._mat4f("uView", ((CEScene) CEngine.SCENE.getCurrentScene()).getCamera().getViewMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size()*7*2);

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.stop();
    }

    // ==================================================
    // Add line2D methods
    // ==================================================
    public  int addLine2D(Vector2f from, Vector2f to) {
        return addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }

    public  int addLine2D(Vector2f from, Vector2f to, Vector3f color) {
        return addLine2D(from, to, color, 1);
    }


    int lineCount = 0;
    public int getLineCount() {
        return lineCount;
    }
    public  int addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_LINES) return -1;
        lines.add(new C2DLine(from, to, color, lifetime));
        lineCount++;
        return lines.size()-1;
    }

    // ==================================================
    // Add Box2D methods
    // ==================================================

    public  int[] addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        return addBox2D(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    public  int[] addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
       return addBox2D(center, dimensions, rotation, color, 1);
    }
    public  int[] addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color,float zPos) {
        return addBox2D(center, dimensions, rotation, color, 1,zPos);
    }
    public int[] addBox2D(Vector2f center, Vector2f dimensions, float rotation,
                          Vector3f color, int lifetime,float zPos) {
        // Köşe noktalarını hesapla
        Vector2f min = new Vector2f(-dimensions.x / 2, -dimensions.y / 2);
        Vector2f max = new Vector2f(dimensions.x / 2, dimensions.y / 2);

        // Rotasyon matrisi
        Matrix3x2f rotationMatrix = new Matrix3x2f().rotate(toRadians(rotation));

        // Köşe noktaları
        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y)
        };

        // Köşe noktalarını döndür ve merkeze taşı
        for (int i = 0; i < vertices.length; i++) {
            Vector3f vertex3D = new Vector3f(vertices[i].x, vertices[i].y, 0); // 2D'yi 3D'ye dönüştür
            rotationMatrix.transform(vertex3D); // Rotasyonu uygula
            vertices[i].set(vertex3D.x, vertex3D.y); // 2D noktasını güncelle
            vertices[i].add(center); // Merkeze taş
        }

        return new int[] {
                addLine2D(vertices[0], vertices[1], color, lifetime),
                addLine2D(vertices[1], vertices[2], color, lifetime),
                addLine2D(vertices[2], vertices[3], color, lifetime),
                addLine2D(vertices[3], vertices[0], color, lifetime)
        };
    }
    public int[] addBox2D(Vector2f center, Vector2f dimensions, float rotation,
                          Vector3f color, int lifetime) {
        // Köşe noktalarını hesapla
        Vector2f min = new Vector2f(-dimensions.x / 2, -dimensions.y / 2);
        Vector2f max = new Vector2f(dimensions.x / 2, dimensions.y / 2);

        // Rotasyon matrisi
        Matrix3x2f rotationMatrix = new Matrix3x2f().rotate(toRadians(rotation));

        // Köşe noktaları
        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y)
        };

        // Köşe noktalarını döndür ve merkeze taşı
        for (int i = 0; i < vertices.length; i++) {
            Vector3f vertex3D = new Vector3f(vertices[i].x, vertices[i].y, 0); // 2D'yi 3D'ye dönüştür
            rotationMatrix.transform(vertex3D); // Rotasyonu uygula
            vertices[i].set(vertex3D.x, vertex3D.y); // 2D noktasını güncelle
            vertices[i].add(center); // Merkeze taş
        }

        return new int[] {
                addLine2D(vertices[0], vertices[1], color, lifetime),
                addLine2D(vertices[1], vertices[2], color, lifetime),
                addLine2D(vertices[2], vertices[3], color, lifetime),
                addLine2D(vertices[3], vertices[0], color, lifetime)
        };
    }


    public void removeBox2D(int v1,int v2,int v3,int v4){
        removeLine2D(v1);
        removeLine2D(v2);
        removeLine2D(v3);
        removeLine2D(v4);
    }
    public void removeBox2D(int[] remove){
        for (int j : remove) {
            removeLine2D(j);
        }
    }

    // ==================================================
    // Add Circle methods
    // ==================================================
    public  void addCircle(Vector2f center, float radius) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addCircle(center, radius, new Vector3f(0, 1, 0), 1);
    }

    public  void addCircle(Vector2f center, float radius, Vector3f color) {
        addCircle(center, radius, color, 1);
    }

    public  void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
        Vector2f[] points = new Vector2f[20];
        int increment = 360 / points.length;
        int currentAngle = 0;

        for (int i=0; i < points.length; i++) {
            Vector2f tmp = new Vector2f(0, radius);
            rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if (i > 0) {
                addLine2D(points[i - 1], points[i], color, lifetime);
            }
            currentAngle += increment;
        }

        addLine2D(points[points.length - 1], points[0], color, lifetime);
    }
    public  Vector2f rotate(Vector2f vec, float angleDeg, Vector2f origin) {
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        float cos = (float)Math.cos(Math.toRadians(angleDeg));
        float sin = (float)Math.sin(Math.toRadians(angleDeg));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        xPrime += origin.x;
        yPrime += origin.y;

        vec.x = xPrime;
        vec.y = yPrime;
        return vec;
    }

    @Override
    public void init() {
        start();
    }

    @Override
    public void update(double dt) {
        beginFrame();
        draw();
    }

    @Override
    public void dispose() {
    }
}
