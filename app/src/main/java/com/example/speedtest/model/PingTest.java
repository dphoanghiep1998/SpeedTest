package com.example.speedtest.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PingTest extends Thread{
    // De test ping ta can su dung processbuilder de chay cau lenh cmd trong java,
    // cau lenh ping thi ai cung biet roi
    //ping <optional> <arg> <server>
    //o day ta dung "count" optional
    //co nghia la de ping "count" lan`
    //xu ly tinh huong ta thay:
    // cac dong co icmp sequence la nhung dong ping, hien thi thong so moi lan ping
    // co thong so Rtt(thoi gian de yeu cau mang di tu diem bat dau den diem ket thuc roi tro ve diem bat dau)
    //thong so avgRtt la Rtt trung binh
    // thong so packetLoss
    String server = "";
    int count;
    String loss;
    double instantRtt = 0;
    double avgRtt = 0.0;
    boolean finished = false;
    boolean started = false;

    public PingTest(String serverIpAddress, int pingTryCount) {
        this.server = serverIpAddress;
        this.count = pingTryCount;
    }

    public double getAvgRtt() {
        return avgRtt;
    }

    public double getInstantRtt() {
        return instantRtt;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        try{
            ProcessBuilder ps = new ProcessBuilder("ping","-c"+count, this.server);
            ps.redirectErrorStream(true);
            Process pr = ps.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("icmp_seq")) {
                    instantRtt = Double.parseDouble(line.split(" ")[line.split(" ").length - 2].replace("time=", ""));
                }
                if (line.startsWith("rtt ")) {
                    avgRtt = Double.parseDouble(line.split("/")[4]);
                    break;
                }
                if(line.contains("packet loss")){
                    loss = line.split(",")[2].trim().split(" ")[0];
                }
                if (line.contains("Unreachable") || line.contains("Unknown") || line.contains("%100 packet loss")) {
                    return;
                }
            }
            pr.waitFor();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finished = true;
    }
}
