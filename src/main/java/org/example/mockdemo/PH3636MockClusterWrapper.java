package org.example.mockdemo;

public class PH3636MockClusterWrapper implements Cluster {
    private Cluster cluster;

    public PH3636MockClusterWrapper(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public Invokerjoin(Directory directory) throws RpcException {
        return new PH3636MockClusterInvoker(directory, this.cluster.join(directory));
    }
}
