package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicProp {
    private String name;
    private int partitions;
    private short replica;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPartitions() {
        return partitions;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public short getReplica() {
        return replica;
    }

    public void setReplica(short replica) {
        this.replica = replica;
    }

    @Override
    public String toString() {
        return "TopicProp{" +
                "name='" + name + '\'' +
                ", partitions=" + partitions +
                ", replica=" + replica +
                '}';
    }
}
