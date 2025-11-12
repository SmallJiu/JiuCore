package cat.jiu.core.api.serializable;

import cat.jiu.core.api.IData;

public interface IDataSerializable<T extends IData<?>> {
    void read(T data);
    T write(T data);
}
