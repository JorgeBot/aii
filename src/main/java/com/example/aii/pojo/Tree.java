package com.example.aii.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tree<E> implements Serializable {

    public enum TYPE {LEAF, BRANCH, TRUNK}

    private Long id;
    private Long parentId;
    private List<Tree<E>> children;
    private String name;
    private TYPE type;
    private boolean checked;
    private E element;

    public Tree(Long id, Long parentId, String name, TYPE type, boolean checked, E element) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.checked = checked;
        this.element = element;
        this.type = type;
    }

    public Tree(Long id, Long parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    public Tree() {
    }

    /**
     * 合并层级
     *
     * @param trees
     * @return
     */
    public static <T> List<Tree<T>> nesting(List<Tree<T>> trees) {
        List<Tree<T>> trunks = trees.stream().filter(f -> f.getType().equals(TYPE.TRUNK)  || f.getParentId() == null).collect(Collectors.toList());
        trunks.forEach(e -> pointer(trees, e));
        return trunks;
    }

    private static <T> void pointer(List<Tree<T>> trees, Tree<T> pointer) {
        List<Tree<T>> children = trees.stream().filter(e -> e.getParentId() != null && e.getParentId().equals(pointer.getId())).collect(Collectors.toList());
        children.forEach(e -> pointer(trees, e));
        pointer.setChildren(children);
    }

    /**
     * 展开层级
     *
     * @param trees
     * @return
     */
    public static <T> List<Tree<T>> tiling(List<Tree<T>> trees) {
        List<Tree<T>> res = new ArrayList<>();
        iterator(trees, res);
        return res;
    }

    private static <T> void iterator(List<Tree<T>> trees, List<Tree<T>> res) {
        trees.forEach(e -> {
            res.add(e);
            iterator(e.getChildren(), res);
        });
    }

    public static <T> Optional<Tree<T>> findById(List<Tree<T>> trees, Long id) {
        if (trees == null || trees.isEmpty() || id == null) return Optional.empty();
        return trees.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    public static <T> Optional<Tree<T>> findByName(List<Tree<T>> trees, String name) {
        if (trees == null || trees.isEmpty() || name == null) return Optional.empty();
        return trees.stream().filter(f -> f.getName().equals(name)).findFirst();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Tree<E>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<E>> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }
}
