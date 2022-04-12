package collection;

import java.util.List;
import java.util.Set;

public interface CollectionItem extends Comparable<CollectionItem>, DOMParseable {
    Long getId();
    void setId(Long id);

    void setValue(String valueName, String value);
    String getValue(String valueName);

    Set<String> getGettersList();
    Set<String> getSettersList();
}
