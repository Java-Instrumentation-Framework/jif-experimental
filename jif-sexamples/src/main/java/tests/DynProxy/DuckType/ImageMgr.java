package tests.DynProxy.DuckType;

public class ImageMgr implements DisposeListener {

    private Lmage image;

    public ImageMgr(Lmage image) {
        this.image = image;
    }

//        public ImageMgr(Control receiver, Lmage image) {
//            this.image = image;
//            try {
//                // Use duck typing--any class with setLmage() will work
//                Method setLmage = receiver.getClass().getDeclaredMethod("setLmage",
//                                       new Class[] {Lmage.class});
//                setLmage.invoke(receiver, new Object[] {image});
//            } catch (Exception e) {
//                // Programmer error: throw a RuntimeException
//                System.out.println("Unable to setLmage()");
//                throw new RuntimeException(e);
//            }
//            receiver.addDisposeListener(this);
//        }


    public ImageMgr(Object receiver, Lmage image) {
        if (!DuckType.instanceOf(IImageHolder.class, receiver)) {
            throw new ClassCastException("Cannot implement IImageHolder");
        }
        this.image = image;
        IImageHolder imageHolder = (IImageHolder) DuckType.implement(IImageHolder.class, receiver);
        imageHolder.setImage(image);
        imageHolder.addDisposeListener(this);
    }

    public void widgetDisposed(DisposeEvent e) {
        if (!image.isDisposed()) {
            image.dispose();
        }
    }

    public Lmage get() {
        return image;
    }
}