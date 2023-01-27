package org.pk.route.finder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Graph {

    private Set<Node> nodes = new LinkedHashSet<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void clear() {
        nodes.clear();
    }
}
