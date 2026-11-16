package teste_opengl.teste_opengl;

import static org.lwjgl.opengl.GL11.*;

public class Camera {
    public static class Vetor3D {
        public float x, y, z;

        public Vetor3D(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vetor3D(Vetor3D other) {
            this(other.x, other.y, other.z);
        }

        public void set(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void normalizar() {
            float len = (float) Math.sqrt(x * x + y * y + z * z);
            if (len > 0.00001f) {
                x /= len;
                y /= len;
                z /= len;
            }
        }

        public float produtoEscalar(Vetor3D v) {
            return this.x * v.x + this.y * v.y + this.z * v.z;
        }

        public Vetor3D produtoVetorial(Vetor3D v) {
            return new Vetor3D(
                    this.y * v.z - this.z * v.y,
                    this.z * v.x - this.x * v.z,
                    this.x * v.y - this.y * v.x);
        }
    }

    private Vetor3D olho;
    private Vetor3D u, v, n;
    private float anguloVisao, razaoAspecto, planoPerto, planoLonge;

    public Camera() {
        this.olho = new Vetor3D(0, 0, 5);
        u = new Vetor3D(1, 0, 0);
        v = new Vetor3D(0, 1, 0);
        n = new Vetor3D(0, 0, 1);
    }

    public void setFormatoCamera(float vAngle, float asp, float nr, float fr) {
        this.anguloVisao = vAngle;
        this.razaoAspecto = asp;
        this.planoPerto = nr;
        this.planoLonge = fr;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        double top = planoPerto * Math.tan(Math.toRadians(anguloVisao / 2.0));
        double bottom = -top;
        double right = top * razaoAspecto;
        double left = -right;
        glFrustum(left, right, bottom, top, planoPerto, planoLonge);

        glMatrixMode(GL_MODELVIEW);
    }

    public void setMatrizModelView() {
        float[] m = new float[16];

        Vetor3D eVec = new Vetor3D(-olho.x, -olho.y, -olho.z);

        m[0] = u.x;
        m[4] = u.y;
        m[8] = u.z;
        m[12] = eVec.produtoEscalar(u);
        m[1] = v.x;
        m[5] = v.y;
        m[9] = v.z;
        m[13] = eVec.produtoEscalar(v);
        m[2] = n.x;
        m[6] = n.y;
        m[10] = n.z;
        m[14] = eVec.produtoEscalar(n);
        m[3] = 0.0f;
        m[7] = 0.0f;
        m[11] = 0.0f;
        m[15] = 1.0f;

        glMatrixMode(GL_MODELVIEW);
        glLoadMatrixf(m);
    }

    public void set(Vetor3D eye, Vetor3D look, Vetor3D up) {
        this.olho.set(eye.x, eye.y, eye.z);

        this.n.set(olho.x - look.x, olho.y - look.y, olho.z - look.z);
        this.n.normalizar();

        this.u = up.produtoVetorial(this.n);
        this.u.normalizar();

        this.v = this.n.produtoVetorial(this.u);
        this.v.normalizar();

        setMatrizModelView();
    }

    public void slide(float delU, float delV, float delN) {
        olho.x += delU * u.x + delV * v.x + delN * n.x;
        olho.y += delU * u.y + delV * v.y + delN * n.y;
        olho.z += delU * u.z + delV * v.z + delN * n.z;
        setMatrizModelView();
    }

    public void roll(float angle) {
        float cs = (float) Math.cos(Math.toRadians(angle));
        float sn = (float) Math.sin(Math.toRadians(angle));

        Vetor3D t = new Vetor3D(u);
        u.set(cs * t.x - sn * v.x, cs * t.y - sn * v.y, cs * t.z - sn * v.z);
        v.set(sn * t.x + cs * v.x, sn * t.y + cs * v.y, sn * t.z + cs * v.z);

        u.normalizar();
        v.normalizar();
        setMatrizModelView();
    }

    public void pitch(float angulo) {
        float cs = (float) Math.cos(Math.toRadians(angulo));
        float sn = (float) Math.sin(Math.toRadians(angulo));

        Vetor3D t = new Vetor3D(v);
        v.set(cs * t.x - sn * n.x, cs * t.y - sn * n.y, cs * t.z - sn * n.z);
        n.set(sn * t.x + cs * n.x, sn * t.y + cs * n.y, sn * t.z + cs * n.z);

        v.normalizar();
        n.normalizar();
        setMatrizModelView();
    }

    public void yaw(float angulo) {
        float cs = (float) Math.cos(Math.toRadians(angulo));
        float sn = (float) Math.sin(Math.toRadians(angulo));

        Vetor3D t = new Vetor3D(n);
        n.set(cs * t.x - sn * u.x, cs * t.y - sn * u.y, cs * t.z - sn * u.z);
        u.set(sn * t.x + cs * u.x, sn * t.y + cs * u.y, sn * t.z + cs * u.z);

        n.normalizar();
        u.normalizar();
        setMatrizModelView();
    }
}
