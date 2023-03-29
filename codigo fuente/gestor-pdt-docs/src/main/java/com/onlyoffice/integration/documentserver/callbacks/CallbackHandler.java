/**
 *
 * (c) Copyright Ascensio System SIA 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.onlyoffice.integration.documentserver.callbacks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.onlyoffice.integration.dto.Track;

@Service
public class CallbackHandler implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Logger logger = LoggerFactory.getLogger(CallbackHandler.class);

    private transient Map<Integer, Callback> callbackHandlers = new HashMap<>();

    public void register(int code, Callback callback){  // register a callback handler
        callbackHandlers.put(code, callback);
    }

    public int handle(Track body, String fileName){  // handle a callback
        Callback callback = callbackHandlers.get(body.getStatus());
        if (callback == null){
            logger.warn("Callback status {} is not supported yet", body.getStatus());
           return 0;
        }

        return callback.handle(body, fileName);
    }
}
