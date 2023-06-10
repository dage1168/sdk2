! regenerator-runtime/runtime.js */ \"./node_modules/regenerator-runtime/runtime.js\");\n/* harmony import */ var
 regenerator_runtime_runtime_js__WEBPACK_IMPORTED_MODULE_1___default = 
 /*#__PURE__*/__webpack_require__.n(regenerator_runtime_runtime_js__WEBPACK_IMPORTED_MODULE_1__);\n/* harmony import */ var _utils_fetchError__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @/utils/fetchError */ \"./src/utils/fetchError.ts\");\n/* harmony import */ var _services_counter__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @/services/counter */ \"./src/services/counter.ts\");\n\n\n//@ts-nocheck\n\n // import { getAllContact } from \"./callback\";\n\nvar w = window;\nvar onlineWebSocket = {\n  // 实例\n  _instance: \"\",\n  // 手动关闭\n  isManualClose: false,\n  // 重连定时器\n  reconnectTimer: {\n    id: 0,\n    delay: 3 * 1000\n  },\n  // 心跳定时器\n  heartbeatTimer: {\n    id: 0,\n    delay: 45 * 1000\n  },\n  WEBSOCKET_STATE_OPEN: 1,\n  // 消息类型\n  MSG_TYPE: {\n    // 强制下线\n    FORCED_OFFLIEN: 210,\n    // 翻译功能权限变化\n    TRANSLATE_PERMISSION_CHANGE: 217,\n    // 工单过期或失效（失效是因为工单到期时间大于统计器到期时间）\n    ORDER_EXPIRED_OR_DISABLED: 218,\n    // 工单自动翻译开关变化\n    ORDER_AUTO_TRANSLATE_SWITCH_CHANGE: 219,\n    // 修改翻译API\n    CHANGE_TRANSLATE_API: 220\n  },\n  connect: function connect() {\n    this._instance = \"start\";\n    this.setMyHeartTimer();\n    return; // const ws = new WebSocket(WEBSOCKET_URL)\n    // this._instance = ws\n    // ws.addEventListener('open', () => {\n    //   console.log('[WebSocket] Open at', new Date().toLocaleString())\n    //   // 重置isManualClose\n    //   this.isManualClose = false\n    //   // 网络正常时，上报上线\n    //   // 2021-03-19 骏宏将online和login事件合并了，只需发送online即可绑定+上线\n    //   if (w.isOnline) {\n    //     w.Counter.reportLogin('WebSocket连接成功')\n    //   }\n    //   // 告知后端工单和账号的绑定关系\n    //   // ws.send(\n    //   //   JSON.stringify({\n    //   //     type: \"login\",\n    //   //     order_id: w._workOrder.order_id,
\n    //   //     line_account: w._user.mid,\n    //   //   })\n    //   // );\n    //   this.setHeartbeatTimer()\n    // })\n    // ws.addEventListener('close', () => {\n    //   // 断开连接时，后端会把账号状态修改为下线\n    //   console.log('[WebSocket] Close at', new Date().toLocaleString())\n    //   if (!this.isManualClose) {\n    //     this.setReconnectTimer()\n    //   }\n    // })\n    // ws.addEventListener('message', async event => {\n    //   const data = JSON.parse(event.data)\n    //   console.log('[WebSocket] Message', data, new Date().toLocaleString())\n    //   switch (data.code) {\n    //     case this.MSG_TYPE.FORCED_OFFLIEN: {\n    //       w.Counter.refresh()\n    //       break\n    //     }\n    //     case this.MSG_TYPE.TRANSLATE_PERMISSION_CHANGE: {\n    //       if (!data.manual_sign) {\n    //         w.Counter.Trans.disableTranslateFunc({\n    //           manual: true\n    //         })\n    //       }\n    //       if (!data.automatic_sign) {\n    //         w.Counter.Trans.disableTranslateFunc({\n    //           auto: true\n    //         })\n    //       }\n    //       if (!data.input_sign) {\n    //         w.Counter.Trans.disableTranslateFunc({\n    //           input: true\n    //         })\n    //       }\n    //       const currentTranslateAPI = w.Counter.Trans.getTranslateAPI()\n    //       if (data.translate_api !== currentTranslateAPI) {\n    //         w.Counter.Trans.changeTranslateAPI(data.translate_api)\n    //       }\n    //       break\n    //     }\n    //     case this.MSG_TYPE.ORDER_EXPIRED_OR_DISABLED: {\n    //       const orderStatus = data.status\n    //       const orderStatusZhMap = {\n    //         [WORK_ORDER_STATUS.EXPIRED]: '到期',\n    //         [WORK_ORDER_STATUS.DISABLED]: '失效'\n    //       }\n    //       const orderStatusZh = orderStatusZhMap[orderStatus]\n    //       toastr.warning(\n    //         `由于工单${orderStatusZh}，所有翻译功能已关闭`,\n    //         `工单已${orderStatusZh}`,\n    //         {\n    //           positionClass: 'toast-top-right',\n    //           closeButton: true,\n    //           timeOut: 0,\n    //           extendedTimeOut: 0\n    //         }\n    //       )\n    //       // 关闭所有翻译功能\n    //       w.Counter.Trans.disableTranslateFunc({\n    //         manual: true,\n    //         auto: true,\n    //         input: true\n    //       })\n    //       break\n    //     }\n    //     case this.MSG_TYPE.ORDER_AUTO_TRANSLATE_SWITCH_CHANGE: {\n    //       const swtichStatus = data.status\n    //       // 关闭自动翻译\n    //       if (swtichStatus === 0) {\n    //         toastr.warning(\n    //           '当前工单的自动翻译功能已被关闭',\n    //           '自动翻译功能已关闭',\n    //           {\n    //             positionClass: 'toast-top-right',\n    //             closeButton: true,\n    //             timeOut: 0,\n    //             extendedTimeOut: 0\n    //           }\n    //         )\n    //         w.Counter.Trans.disableTranslateFunc({\n    //           auto: true\n    //         })\n    //         // 开启自动翻译\n    //       } else {\n    //         w.Counter.Trans.enableTranslateFunc().then(({ autoTranslateSwitch }) => {\n    //           if (autoTranslateSwitch) {\n    //             toastr.success(\n    //               '当前工单的自动翻译功能已开启',\n    //               '自动翻译功能已开启',\n    //               {\n    //                 positionClass: 'toast-top-right',\n    //                 closeButton: true\n    //               }\n    //             )\n    //           }\n    //         })\n    //       }\n    //       break\n    //     }\n    //     case this.MSG_TYPE.CHANGE_TRANSLATE_API: {\n    //       const api = data.value\n    //       w.Counter.Trans.changeTranslateAPI(api)\n    //       break\n    //     }\n    //     default: {\n    //       break\n    //     }\n    //   }\n    // })\n    // TEST: 连接关闭\n    // setTimeout(() => {\n    //   this._instance.close()\n    // }, 5000)\n  },\n  close: function close(isManualClose) {\n    if (this._instance) {\n      this.isManualClose = isManualClose; // this._instance.close()\n\n      this._instance = null;\n      this.clearMyHeartTimer();\n    }\n  },\n  reset: function reset() {\n    this.close();\n    this.clearMyHeartTimer();\n    this.connect();\n  },\n  setReconnectTimer: function setReconnectTimer() {\n    var _this = this;\n\n    this.reconnectTimer.id = setTimeout(function () {\n      _this.reset();\n    }, this.reconnectTimer.delay);\n  },\n  cleanReconnectTimer: function cleanReconnectTimer() {\n    this.reconnectTimer.id = clearTimeout(this.reconnectTimer.id);\n  },\n  setHeartbeatTimer: function setHeartbeatTimer() {\n    var _this2 = this;\n\n    this.heartbeatTimer.id = setTimeout(function () {\n      if (_this2._instance && _this2._instance.readyState === _this2.WEBSOCKET_STATE_OPEN) {\n        _this2._instance.send('{type: \"heart\"}');\n      }\n\n      _this2.setHeartbeatTimer();\n    }, this.heartbeatTimer.delay);\n  },\n  clearHeartbeatTimer: function clearHeartbeatTimer() {\n    this.heartbeatTimer.id = clearTimeout(this.heartbeatTimer.id);\n  },\n  setMyHeartTimer: function setMyHeartTimer() {\n    var _this3 = this;\n\n    this.heartbeatTimer.id = setTimeout(function () {\n      if (_this3._instance == \"start\") {\n        Object(_services_counter__WEBPACK_IMPORTED_MODULE_3__[\"ReportHeart\"])({\n          order_id: w._workOrder.order_id,\n          line_account: w._user.config.uid\n        }).then( /*#__PURE__*/function () {\n          var _ref = Object(_Users_lin_jsq_zalo_node_modules_babel_runtime_helpers_esm_asyncToGenerator_js__WEBPACK_IMPORTED_MODULE_0__[\"default\"])( /*#__PURE__*/regeneratorRuntime.mark(function _callee(res) {\n            var logData;\n            return regeneratorRuntime.wrap(function _callee$(_context) {\n              while (1) {\n                switch (_context.prev = _context.next) {\n                  case 0:\n                    console.log(\"[ReportHeart] success\", new Date().toLocaleString());\n\n                    if (res.code === 200 && res.data.lines_key_value == 1) {\n                      console.log(\"[ReportHeart] 需要上报数据\"); // const allContact: any = await getAllContact();\n\n                      logData = []; // allContact.map((item) => {\n                      //   logData.push({\n                      //     version: version,\n                      //     attributes: item.attributes,\n                      //     createdTime: item.createdTime,\n                      //     displayName: item.displayName,\n                      //     mid: item.mid,\n                      //     friendRequestStatus: item.friendRequestStatus,\n                      //     recommendParams: item.recommendParams,\n                      //     relation: item.relation,\n                      //     settings: item.settings,\n                      //     status: item.status,\n                      //     type: item.type,\n                      //   });\n                      // });\n\n                      console.log(\"[ReportHeart] 上报粉丝日志：\", logData);\n                      Object(_utils_fetchError__WEBPACK_IMPORTED_MODULE_2__[\"fetchData\"])(1, w._workOrder.order_id, w._user.config.uid, logData);\n                    }\n\n                  case 2:\n                  case \"end\":\n                    return _context.stop();\n                }\n              }\n            }, _callee);\n          }));\n\n          return function (_x) {\n            return _ref.apply(this, arguments);\n          };\n        }()).catch(function (err) {\n          console.error(\"[ReportHeart]\", err, new Date().toLocaleString());\n        });\n      }\n\n      _this3.setMyHeartTimer();\n    }, this.heartbeatTimer.delay);\n  },\n  clearMyHeartTimer: function clearMyHeartTimer() {\n    this.heartbeatTimer.id = clearTimeout(this.heartbeatTimer.id);\n  }\n};\n/* harmony default export */ __webpack_exports__[\"default\"] = (onlineWebSocket);\n\n//# sourceURL=webpack:///./src/content/websocket.ts?");