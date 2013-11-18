package tests.DynProxy.DuckType;

public class Control {

    Lmage image;
    DisposeListener dl;

    public void setImage(Lmage image) {
        this.image = image;
    }

    public void addDisposeListener(DisposeListener dl) {
        this.dl = dl;
    }
}
