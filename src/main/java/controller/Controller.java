package controller;

import infra.network.Protocol;

public interface Controller {
    int handler(Protocol recvPt);
}
