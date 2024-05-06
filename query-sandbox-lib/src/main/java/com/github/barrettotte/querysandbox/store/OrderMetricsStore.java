package com.github.barrettotte.querysandbox.store;

public interface OrderMetricsStore {

    void distribution();

    void trends();

    void delta();

    void collect();
}
