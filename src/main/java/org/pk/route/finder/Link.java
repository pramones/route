package org.pk.route.finder;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"node"})
@ToString(of = {"node"})
public class Link {

    public static final int WEIGHT_EQUALS_ONE = 1;

    // direction from Link -> Link.Node
    private Node node;

    // no condition on actual distance weight
    private int weight = WEIGHT_EQUALS_ONE;

    public Link(Node node) {
        this.weight = WEIGHT_EQUALS_ONE;
        this.node = node;
    }
}
