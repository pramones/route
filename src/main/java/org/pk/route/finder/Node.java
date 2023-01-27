package org.pk.route.finder;

import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"cca3"})
@ToString(of = {"cca3"})
public class Node {

    private String cca3;

    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;

    @Singular
    private List<Link> links;

    public Node(String cca3) {
        this.cca3 = cca3;
    }

    public void addLink(Link link) {
        if (links == null) {
            links = new LinkedList<>();
        }
        links.add(link);
    }

    public List<String> asString() {
        List<String> asList = new LinkedList<>();
        this.getShortestPath().stream()
                .forEach((node) -> asList.add(node.getCca3()));
        asList.add(cca3);
        return asList;
    }
}
