package controller;

import infra.network.Protocol;

public interface DefinedController {
    int handler(Protocol recvPt) throws Exception;
}
