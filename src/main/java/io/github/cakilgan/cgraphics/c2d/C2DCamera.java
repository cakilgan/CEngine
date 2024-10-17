package io.github.cakilgan.cgraphics.c2d;

import io.github.cakilgan.cgraphics.CGShader;
import io.github.cakilgan.cgraphics.ICamera;
import io.github.cakilgan.cmath.CMath;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.CESceneComponent;
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class C2DCamera extends ICamera implements CESceneComponent {
    public void zoom(float zoomFactor) {
        // Zoom faktörünü uygulayarak genişliği ve yüksekliği güncelle
        width *= zoomFactor;
        height *= zoomFactor;

        // Yeni sol, sağ, alt ve üst değerleri hesapla
        left = -width/2;
        right = width / 2;
        bottom = -height / 2;
        top = height / 2;

        // Projeksiyonu güncelle
        updateProjection();
        scene.objectSystem().getObjects().forEach(new BiConsumer<CEObjectID, CEObject>() {
            @Override
            public void accept(CEObjectID ceObjectID, CEObject object) {
                object.getComponents().forEach(new BiConsumer<String, CEOComponent>() {
                    @Override
                    public void accept(String s, CEOComponent ceoComponent) {
                        if (ceoComponent instanceof CEOCameraLock) {
                            // Yeni konumu hesapla ve ayarla
                            CEOCameraLock cameraLock = (CEOCameraLock) ceoComponent;
                            Vector2f offset = cameraLock.getAdd();
                            object.getTransform().getPos().set(position.x + offset.x, position.y + offset.y);
                        }
                    }
                });
            }
        });
    }
    public static C2DCamera createForBatch(Vector3f pos,float width,float height){
        C2DCamera camera = new C2DCamera(pos,width,height);
        camera.setProjectionCode("uProjection");
        camera.setViewCode("uView");
        return camera;
    };
    public static C2DCamera createForBatch(){
        C2DCamera camera = new C2DCamera(new Vector3f(),CEngine.WINDOW.getConfig().w,CEngine.WINDOW.getConfig().h);
        camera.setProjectionCode("uProjection");
        camera.setViewCode("uView");
        return camera;
    };
    public static C2DCamera createForBatch(CEScene scene){
        C2DCamera camera = new C2DCamera(scene,new Vector3f(),CEngine.WINDOW.getConfig().w,CEngine.WINDOW.getConfig().h);
        camera.setProjectionCode("uProjection");
        camera.setViewCode("uView");
        return camera;
    };
        public static C2DCamera createIsometric(CEScene scene){
            C2DCamera camera = new C2DCamera(scene,new Vector3f(),CEngine.WINDOW.getConfig().w,CEngine.WINDOW.getConfig().h);
            //amera.position.set(0, 0, 10); // Kameranın yüksekliği
            camera.front.set(0, -1, 1); // Yukarıdan eğik bakış
            camera.up.set(0, 1, 0); // Yukarı doğru yön
            camera.position.set(0, 20, 20); // Yüksekliği artır

            //float width = 100f; // Haritanın genişliğine göre ayarla
            //float height = 100f; // Haritanın yüksekliğine göre ayarla
            camera.left = -1f;
            camera.right = camera.width;
            camera.bottom = -1f;
            camera.top = camera.height;
            CEngine.LOGGER.info("left: "+camera.left+
                    " right: "+camera.right+
                    " bottom: "+camera.bottom+
                    " top: "+camera.top);
            camera.zNear = -1400f;
            camera.zFar = 1400f;


            return camera;
        }
    private float left, right, bottom, top;

    public C2DCamera(){
        this(new Vector3f(),CEngine.WINDOW.getConfig().w,CEngine.WINDOW.getConfig().h);
    }
    float width,height;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    CEScene scene;
    public C2DCamera(CEScene scene, Vector3f pos, float width, float height) {
        this.position = pos;
        this.left = -1.0f;
        this.right =width;
        this.bottom = -1.0f;
        this.top = height;
        this.projection = new Matrix4f();
        this.view = new Matrix4f();
        this.width = width;
        this.height = height;
        //-1.0f, +1.0f, -1.0f, +1.0f, +1.0f, -1.0f
        setProjectionCode("projection");
        setViewCode("view");
        updateProjection();
        front = new Vector3f(0.0f, 0.0f, 1.0f);
        up = new Vector3f(0.0f, 1.0f, 2.0f);
        setzNear(-10f);
        setzFar(100f);
        this.scene = scene;
    }
    public C2DCamera( Vector3f pos, float width, float height) {
        this.position = pos;
        this.left = -1.0f;
        this.right =width;
        this.bottom = -1.0f;
        this.top = height;
        this.projection = new Matrix4f();
        this.view = new Matrix4f();
        this.width = width;
        this.height = height;
        //-1.0f, +1.0f, -1.0f, +1.0f, +1.0f, -1.0f
        setProjectionCode("projection");
        setViewCode("view");
        updateProjection();
        front = new Vector3f(0.0f, 0.0f, 1.0f);
        up = new Vector3f(0.0f, 1.0f, 2.0f);
        setzNear(-1f);
        setzFar(3f);
    }
    public Vector3f getPosition(){
        return this.position;
    }

    public void updateProjection() {
        projection.identity();
        projection.ortho(left, right, bottom, top, zNear, zFar);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double dt) {
        refresh();
        render(CEngine.BATCH_SHADER);
    }

    @Override
    public void dispose() {

    }

    boolean canMoveWithWASD = true;
    public void setCanMoveWithWASD(boolean canMoveWithWASD) {
        this.canMoveWithWASD = canMoveWithWASD;
    }

    @Override
    public void refresh() {
        if (canMoveWithWASD){
            if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_W)) {
                position.y += (float) (1000f * CEngine.TIME.getDt());
            }
            if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_S)) {
                position.y -= (float) (1000f * CEngine.TIME.getDt());
            }
            if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_A)) {
                position.x += (float) (1000f * CEngine.TIME.getDt());
            }
            if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_D)) {
                position.x -= (float) (1000f * CEngine.TIME.getDt());
            }
        }
        scene.objectSystem().getObjects().forEach(new BiConsumer<CEObjectID, CEObject>() {
            @Override
            public void accept(CEObjectID ceObjectID, CEObject object) {
                final boolean[] has = new boolean[1];
                object.getComponents().forEach(new BiConsumer<String, CEOComponent>() {
                    @Override
                    public void accept(String s, CEOComponent ceoComponent) {
                        if (ceoComponent instanceof CEOCameraLock){
                            object.getTransform().getPos().set(position.x+((CEOCameraLock) ceoComponent).getAdd().x,position.y+((CEOCameraLock) ceoComponent).getAdd().y);
                        }
                    }
                });
            }
        });
        updateProjection(); // Kamera her hareket ettiğinde projeksiyonu güncelle
    }

    String projectionCode, viewCode;

    public void setProjectionCode(String projectionCode) {
        this.projectionCode = projectionCode;
    }
    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    @Override
    public void render(CGShader shader) {
        shader._mat4f(projectionCode, projection);
        shader._mat4f(viewCode, getViewMatrix());
    }
    public void printViewMatrix() {
        System.out.println("msg: View Matrix:");
        float[] matrixData = new float[16];
        view.get(matrixData);

        // 4x4 matrisin satır satır yazdırılması
        for (int i = 0; i < 4; i++) {
            System.out.print("msg: [ ");
            for (int j = 0; j < 4; j++) {
                System.out.printf("msg: %8.4f ", matrixData[i * 4 + j]);
            }
            System.out.println("msg: ]");
        }
    }
    public Matrix4f getViewMatrix() {
        this.view = new Matrix4f().identity();
        Vector3f add = new Vector3f();
        position.add(front,add);
        this.view.lookAt(position, add, up);
        return this.view;
    }


    public void _default() {
        render(CEngine.DEFAULT_SHADER);
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }
    public Vector3f screenToWorld(float screenX, float screenY) {
        // Ekran boyutları
        float windowWidth = CEngine.WINDOW.getConfig().w;
        float windowHeight = CEngine.WINDOW.getConfig().h;

        // Normalleştirilmiş cihaz koordinatları (NDC)
        float ndcX = (2.0f * screenX) / windowWidth - 1.0f;
        float ndcY = 1.0f - (2.0f * screenY) / windowHeight; // Y eksenini ters çevirmek gerek

        // Z'yi 0.0f alıyoruz (ekran düzleminde, far plane değil)
        Vector4f clipCoords = new Vector4f(ndcX, ndcY, 0.0f, 1.0f);

        // Görünüm ve projeksiyon matrislerinin tersini al
        Matrix4f invProjection = new Matrix4f(projection).invert();
        Matrix4f invView = new Matrix4f(getViewMatrix()).invert();

        // Clip space'den view space'e dönüşüm
        Vector4f viewCoords = invProjection.transform(clipCoords);

        // Homogeneous divide (perspective division)
        if (viewCoords.w != 0.0f) {
            viewCoords.div(viewCoords.w); // Clip-space -> NDC -> View-space dönüşümü
        }

        // View space'den dünya koordinatlarına dönüşüm
        Vector4f worldCoords = invView.transform(viewCoords);

        return new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
    }
    public Vector2f worldToScreen(Vector3f worldPos) {
        // Dünya koordinatlarını clip space'e çevir
        Vector4f worldCoords = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1.0f);

        // View ve Projection matrislerini uygula
        Vector4f clipCoords = projection.transform(new Vector4f(view.transform(worldCoords)));

        // Perspektif bölmeyi uygula
        if (clipCoords.w != 0.0f) {
            clipCoords.div(clipCoords.w);
        }

        // NDC'den ekran koordinatlarına çevir
        float windowWidth = CEngine.WINDOW.getConfig().w;
        float windowHeight = CEngine.WINDOW.getConfig().h;

        float screenX = (clipCoords.x * 0.5f + 0.5f) * windowWidth;
        float screenY = (1.0f - (clipCoords.y * 0.5f + 0.5f)) * windowHeight;

        return new Vector2f(screenX, screenY);
    }




    public List<CEObject> objects = new ArrayList<>();
    public void staticObject(CEObject object){
        objects.add(object);
    }

    //I copied this method from azurite engine.
    public void smoothFollow(Vector2f c) {

        float smoothing = 0.045f;
        Vector2f desiredPosition = new Vector2f(c.x + CEngine.WINDOW.getConfig().w / 2f, c.y - CEngine.WINDOW.getConfig().h / 2f);
        Vector2f smoothedPosition = new Vector2f(CMath.lerp(position.x, desiredPosition.x, smoothing),
                CMath.lerp(position.y, desiredPosition.y, smoothing));
        // If you notice black bars while the camera is panning, it is because floating
        // point positions can cause discrepencies, unfortunately casing the lerp to an
        // int makes the motion a little bit choppy
        if (CMath.dist(desiredPosition, position) < 10) {
            position.x  = desiredPosition.x;
            position.y = desiredPosition.y;
        }
        position.x = smoothedPosition.x;
        position.y = smoothedPosition.y;
    }
}
