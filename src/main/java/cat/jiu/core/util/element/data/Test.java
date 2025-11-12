package cat.jiu.core.util.element.data;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.serializable.IDataSerializable;
import cat.jiu.core.util.JsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class Test implements IDataSerializable<IData.IMapData<?>> {
    public static void main(String... args) {
        String json = JsonUtils.GSON_FORMAT.toJson(testJson());
        System.out.println(json);
        Test jsonData = testJson(JsonParser.parseString(json).getAsJsonObject());
        System.out.println(jsonData);
    }
    public static JsonObject testJson() {
        JsonData.MapData data = JsonData.map();
        new Test(new Random()).write(data);
        return data.getData();
    }
    public static Test testJson(JsonObject data) {
        Test test = new Test();
        test.read(JsonData.map(data));
        return test;
    }
    public static CompoundTag testNBT() {
        NBTData.MapData data = NBTData.map();
        new Test(new Random()).write(data);
        return data.getData();
    }
    public static Test testNBT(CompoundTag data) {
        Test test = new Test();
        test.read(NBTData.map(data));
        return test;
    }



    public byte byteData;
    public short shortData;
    public int intData;
    public long longData;

    public float floatData;
    public double doubleData;

    public boolean booleanData;
    public String stringData;

    public Test() {}
    public Test(Random random) {
        this.byteData = (byte) random.nextInt();
        this.shortData = (short) random.nextInt();
        this.intData = random.nextInt();
        this.longData = random.nextLong();

        this.floatData = random.nextFloat();
        this.doubleData = random.nextDouble();

        this.booleanData = random.nextBoolean();
        this.stringData = String.valueOf(random.nextLong());

        for (int i = 0; i < random.nextInt(1, 10); i++) {
            this.addData(new Test1(random));
        }
        for (int i = 0; i < random.nextInt(1, 10); i++) {
            this.addData(String.valueOf(UUID.randomUUID()), new Test2(random));
        }
    }

    public static class Test1 extends Test{
        public Test1(Random random) {
            this.byteData = (byte) random.nextInt();
            this.shortData = (short) random.nextInt();
            this.intData = random.nextInt();
            this.longData = random.nextLong();

            this.floatData = random.nextFloat();
            this.doubleData = random.nextDouble();

            this.booleanData = random.nextBoolean();
            this.stringData = String.valueOf(random.nextLong());
            for (int i = 0; i < random.nextInt(1, 5); i++) {
                this.addData(new Test2(random));
            }
            for (int i = 0; i < random.nextInt(1, 5); i++) {
                this.addData(String.valueOf(UUID.randomUUID()), new Test2(random));
            }
        }

        @Override
        public IData.IMapData<?> write(IData.IMapData<?> data) {
            super.write(data);
            data.putData("id", "Test - 1 - " + UUID.randomUUID());
            return data;
        }
    }
    public static class Test2 extends Test{
        public Test2(Random random) {
            this.byteData = (byte) random.nextInt();
            this.shortData = (short) random.nextInt();
            this.intData = random.nextInt();
            this.longData = random.nextLong();

            this.floatData = random.nextFloat();
            this.doubleData = random.nextDouble();

            this.booleanData = random.nextBoolean();
            this.stringData = String.valueOf(random.nextLong());
        }

        @Override
        public IData.IMapData<?> write(IData.IMapData<?> data) {
            super.write(data);
            data.putData("id", "Test - 2 - " + UUID.randomUUID());
            return data;
        }
    }

    public List<Test> listData;
    public void addData(Test data) {
        if (listData == null) {
            listData = new ArrayList<>();
        }
        listData.add(data);
    }
    public Map<String, Test> mapData;
    public void addData(String key, Test data) {
        if (mapData == null) {
            mapData = new HashMap<>();
        }
        mapData.put(key, data);
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.byteData = data.getByte("byte", (byte) 0);
        this.shortData = data.getShort("short", (short) 0);
        this.intData = data.getInt("int", 0);
        this.longData = data.getLong("long", 0);
        this.floatData = data.getFloat("float", 0f);
        this.doubleData = data.getDouble("double", 0d);

        this.booleanData = data.getBoolean("boolean", false);
        this.stringData = data.getString("string", "not found data");

        data.getList("list", IData.IMapData.class).foreach((k, v) -> {
            if (v!=null){
                Test object = new Test();
                object.read(v.getAsMap());
                this.addData(object);
            }
        });
        data.getMap("map").foreach((k, v)->{
            if (v!=null){
                Test object = new Test();
                object.read(v.getAsMap());
                this.addData(k, object);
            }
        });
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        data.putData("id", "Test - 0 - " + UUID.randomUUID());
        data.putData("byte", this.byteData);
        data.putData("short", this.shortData);
        data.putData("int", this.intData);
        data.putData("long", this.longData);

        data.putData("float", this.floatData);
        data.putData("double", this.doubleData);

        data.putData("boolean", this.booleanData);
        data.putData("string", this.stringData);

        if (mapData != null) {
            IData.IMapData<?> mapData = data.newMap();
            this.mapData.forEach((k,v)->
                mapData.putData(k, v.write(data.newMap()))
            );
            data.putData("map", mapData);
        }
        if (listData != null) {
            IData.IListData<?> listData = data.newList();
            this.listData.forEach(v->
                    listData.putData(v.write(data.newMap()))
            );
            data.putData("list", listData);
        }
        return data;
    }
}
