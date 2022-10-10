// IHermesService.aidl
package com.karashok.demohermes;

// Declare any non-default types here with import statements

import com.karashok.demohermes.IRequest;
import com.karashok.demohermes.IResponse;

interface IHermesService {

    Response send(in Request request);
}