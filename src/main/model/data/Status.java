package model.data;


//Interface for volatile and non volatile statuses.
public interface Status {
    boolean isVolatile();

    String getName();
}
