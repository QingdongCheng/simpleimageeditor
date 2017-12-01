package edu.purdue.qingdong.simpleimageeditor;


/**
 * Created by qd on 11/30/17.
 */
public class Image {

    // private variables
    static int counter = 0;
    int _id;
    String _name;
    byte[] _image;

    public Image() {

    }

    // constructor
    public Image(int keyId, String name, byte[] image) {
        this._id = keyId;
        this._name = name;
        this._image = image;
        counter++;

    }

    // constructor
    public Image(String name, byte[] image) {
        this._name = name;
        this._image = image;
        counter++;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    public int getCounter() {
        return counter;
    }
    // setting id
    public void setID(int keyId) {
        this._id = keyId;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting image
    public byte[] getImage() {
        return this._image;
    }

    // setting image
    public void setImage(byte[] image) {
        this._image = image;
    }

}
