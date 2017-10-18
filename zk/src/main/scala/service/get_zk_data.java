package service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class get_zk_data {
    public String getdata(String path) throws IOException, KeeperException, InterruptedException {
        String connectionString = "10.64.21x.xxx:2181";
        int sessionTimeout = 30000;
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper(connectionString,sessionTimeout,null);
        Stat stat = new Stat();

        //byte[] databyte = zk.getData("/FirstZnode",false,stat);
        byte[] databyte = zk.getData(path,false,stat);
        String data = new String(databyte);
        //System.out.println(data);
        zk.close();
        return data;
    }

}
