package org.buildobjects.artifacts.resources;

import java.util.*;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:37:31 PM
 */
public class InMemoryResources implements WritableResources{
    private static class Node {
        final String nodeName;
       private Map<String, Resource> resources = new HashMap<String, Resource>();
        List<Node> children = new ArrayList<Node>();

        private Node(String nodeName) {
            this.nodeName = nodeName;
        }

        public Node childNode(String name){
            for (Node node : children) {
                if (name.equals(node.nodeName)) {
                    return node;
                }
            }
            return null;
        }

        public void addResource(String nameFragment, Resource resource){
            resources.put(nameFragment, resource);
        }

        public Resource resource(String nodeFragment){
            return resources.get(nodeFragment);
        }

        public Node getOrCreateChild(String fragment) {

            Node node = childNode(fragment);
            if (node == null) {
                node = new Node(fragment);
                children.add(node);
            }
            return node;
        }

        public Collection<Resource> resources(){
            return resources.values();
        }

        public Collection<Resource> resourcesRecursively(){
          return resourcesRecursively(new LinkedList<Resource>());
        }

        private Collection<Resource> resourcesRecursively(List<Resource> newResources) {
            newResources.addAll(resources());
            for (Node child : children){
               child.resourcesRecursively(newResources);
            }                        
            return newResources;
        }


    }

    public InMemoryResources() {
    }

    public static Resources resources(Resource... resources){
        return new InMemoryResources(resources);
    }

    public InMemoryResources(Resource... resources) {

        for (Resource resource : resources) {
            this.add(resource);
        }
    }

    Node rootPackage = new Node("");

    public void add(Resource resource) {
        Node node = findOrCreateNode(resource.getPath().getParent());
        node.addResource(resource.getPath().getName(), resource);       
    }

    private Node findOrCreateNode(Path path) {
        Node node = rootPackage;
        if (path == null){
            return node;
        }
        for (String fragment : path.components){
            node = node.getOrCreateChild(fragment);

        }
        return node;
    }

    private Node findNode(Path path) {
        Node node = rootPackage;
        if (path == null){
            return node;
        }
        for (String fragment : path.components){            
            node = node.childNode(fragment);
            if (node == null) {
                return null;
            }

        }
        return node;
    }


    public void deleteResource(Path path) {
        Node parent = findNode(path.getParent());
        if (parent.resources.containsKey(path.getName())){
            parent.resources.remove(path.getName());
        }
    }

    public boolean hasResource(Path path) {
        Node node = findNode(path.getParent());
        return node != null && node.resource(path.getName()) != null;
    }

    public Resource getResource(Path path) {
        Node node = findNode(path.getParent());
        if (node == null) return null;
        return node.resource(path.getName());
         
    }

    public Collection<Resource> getAll() {
        return rootPackage.resourcesRecursively();
    }

    public Collection<Resource> getResourcesInPath(Path path) {
        final Node node = findNode(path);
        if (node == null) return Collections.emptySet();
        return node.resources();
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        final Node node = findNode(path);
        if (node == null) return Collections.emptySet();
        return node.resourcesRecursively();

    }
}
