package org.pk.route.finder;

import lombok.Builder;
import org.pk.route.exception.CountryNotFoundException;
import org.pk.route.model.Country;
import org.pk.route.model.Route;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RouteFinder {

    private final String origin;
    private final String destination;

    private Node originNode;
    private Node destinationNode;
    private Graph graph;
    private Map<String, Node> nodeByCca3;

    @Builder
    public RouteFinder(String origin, String destination, List<Country> countries) {
        this.origin = origin;
        this.destination = destination;
        buildGraph(countries);
    }


    public void buildGraph(final List<Country> counties) {
        nodeByCca3 = new HashMap<>(counties.size());
        graph = new Graph();
        counties.stream()
                .map((country) -> new Node(country.getCca3()))
                .forEach((node) -> {
                    graph.addNode(node);
                    nodeByCca3.put(node.getCca3(), node);
                });
        counties.stream()
                .forEach((country) -> {
                    Node node = nodeByCca3.get(country.getCca3());
                    List<String> borders = country.getBorders();
                    if (borders != null) {
                        borders.stream()
                                .forEach((border) -> {
                                    node.addLink(new Link(nodeByCca3.get(border)));
                                });
                    }
                });
        originNode = nodeByCca3.get(origin);
        if (originNode == null) throw new CountryNotFoundException("Origin country not found");
        destinationNode = nodeByCca3.get(destination);
        if (destinationNode == null) throw new CountryNotFoundException("Destination country not found");
    }

    public Optional<Route> findRoute() {
        calculateDistances(originNode);
        if (destinationNode.getDistance() < Integer.MAX_VALUE) {
            Route route = new Route(destinationNode.asString());
            return Optional.of(route);
        } else {
            return Optional.empty();
        }
    }

    public void calculateDistances(Node originNode) {
        originNode.setDistance(0);
        Set<Node> checkedNodes = new HashSet<>();
        Set<Node> uncheckedNodes = new HashSet<>();
        uncheckedNodes.add(originNode);
        while (uncheckedNodes.size() != 0) {
            Node node = findLowestDistanceNode(uncheckedNodes);
            uncheckedNodes.remove(node);
            List<Link> links = node.getLinks();
            if (links != null) {
                for (Link link : links) {
                    Node linkedNode = link.getNode();
                    Integer linkWeight = link.getWeight();
                    if (!checkedNodes.contains(linkedNode)) {
                        updateShortestRoute(node, linkedNode, linkWeight);
                        uncheckedNodes.add(linkedNode);
                    }
                }
                checkedNodes.add(node);
            }
        }
    }

    private static Node findLowestDistanceNode(Set<Node> uncheckedNodes) {
        Node node = null;
        int distance = Integer.MAX_VALUE;
        for (Node uncheckedNode : uncheckedNodes) {
            int nodeDistance = uncheckedNode.getDistance();
            if (nodeDistance < distance) {
                distance = nodeDistance;
                node = uncheckedNode;
            }
        }
        return node;
    }

    private static void updateShortestRoute(Node originNode, Node fromNode, Integer distance) {
        Integer originDistance = originNode.getDistance();
        if (originDistance + distance < fromNode.getDistance()) {
            fromNode.setDistance(originDistance + distance);
            LinkedList<Node> shortestPath = new LinkedList<>(originNode.getShortestPath());
            shortestPath.add(originNode);
            fromNode.setShortestPath(shortestPath);
        }
    }
}
