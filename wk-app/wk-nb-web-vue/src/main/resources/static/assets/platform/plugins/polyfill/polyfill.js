/**
 * polyfill for Function
 */

// from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind
if (!Function.prototype.bind) {
  Function.prototype.bind = function (oThis) {
    if (typeof this !== 'function') {
      throw new TypeError('Function.prototype.bind - what is trying to be bound is not callable');
    }
    var args = Array.prototype.slice.call(arguments, 1);
    var fToBind = this;
    var fNOP = function () {};
    var fBound = function () {
      return fToBind.apply(this instanceof fNOP ? this : oThis, args.concat(Array.prototype.slice.call(arguments)));
    };

    if (this.prototype) {
      fNOP.prototype = this.prototype;
    }
    fBound.prototype = new fNOP();
    return fBound;
  };
}
/**
 * polyfill for Object
 */

// from https://github.com/es-shims/es5-shim/blob/master/es5-sham.js
(function () {
  var prototypeOfObject = Object.prototype;
  var call = Function.call;
  var owns = call.bind(prototypeOfObject.hasOwnProperty);
  var isEnumerable = call.bind(prototypeOfObject.propertyIsEnumerable);
  var toStr = call.bind(prototypeOfObject.toString);

  var defineGetter;
  var defineSetter;
  var lookupGetter;
  var lookupSetter;
  var supportsAccessors = owns(prototypeOfObject, '__defineGetter__');
  if (supportsAccessors) {
    /* eslint-disable no-underscore-dangle, no-restricted-properties */
    defineGetter = call.bind(prototypeOfObject.__defineGetter__);
    defineSetter = call.bind(prototypeOfObject.__defineSetter__);
    lookupGetter = call.bind(prototypeOfObject.__lookupGetter__);
    lookupSetter = call.bind(prototypeOfObject.__lookupSetter__);
    /* eslint-enable no-underscore-dangle, no-restricted-properties */
  }

  var isPrimitive = function isPrimitive(o) {
    return o == null || (typeof o !== 'object' && typeof o !== 'function');
  };

  if (!Object.getPrototypeOf) {
    Object.getPrototypeOf = function getPrototypeOf(object) {
      var proto = object.__proto__;
      if (proto || proto === null) {
        return proto;
      }
      if (toStr(object.constructor) === '[object Function]') {
        return object.constructor.prototype;
      }
      if (object instanceof Object) {
        return prototypeOfObject;
      }
      return null;
    };
  }

  if (!Object.keys) {
    Object.keys = (function() {
      var hasOwnProperty = Object.prototype.hasOwnProperty;
      var hasDontEnumBug = !({ toString: null }).propertyIsEnumerable('toString');
      var dontEnums = [
        'toString',
        'toLocaleString',
        'valueOf',
        'hasOwnProperty',
        'isPrototypeOf',
        'propertyIsEnumerable',
        'constructor'
      ];
      var dontEnumsLength = dontEnums.length;

      return function (obj) {
        if (typeof obj !== 'object' && (typeof obj !== 'function' || obj === null)) {
          throw new TypeError('Object.keys called on non-object');
        }
        var result = [], prop, i;

        for (prop in obj) {
          if (hasOwnProperty.call(obj, prop)) {
            result.push(prop);
          }
        }
        if (hasDontEnumBug) {
          for (i = 0; i < dontEnumsLength; i++) {
            if (hasOwnProperty.call(obj, dontEnums[i])) {
              result.push(dontEnums[i]);
            }
          }
        }
        return result;
      };
    }());
  }

  if (!Object.getOwnPropertyNames) {
    Object.getOwnPropertyNames = function getOwnPropertyNames(object) {
      if (object !== Object(object)) {
        throw TypeError('Object.getOwnPropertyNames called on non-object: ' + object);
      }
      return Object.keys(object);
    };
  }

  var doesGetOwnPropertyDescriptorWork = function doesGetOwnPropertyDescriptorWork(object) {
    try {
      object.sentinel = 0;
      return Object.getOwnPropertyDescriptor(object, 'sentinel').value === 0;
    } catch (err) {
      return false;
    }
  };
  var getOwnPropertyDescriptorFallback;
  if (Object.defineProperty) {
    var getOwnPropertyDescriptorWorksOnObject = doesGetOwnPropertyDescriptorWork({});
    var getOwnPropertyDescriptorWorksOnDom = typeof document === 'undefined' ||
      doesGetOwnPropertyDescriptorWork(document.createElement('div'));
    if (!getOwnPropertyDescriptorWorksOnDom || !getOwnPropertyDescriptorWorksOnObject) {
      getOwnPropertyDescriptorFallback = Object.getOwnPropertyDescriptor;
    }
  }
  if (!Object.getOwnPropertyDescriptor || getOwnPropertyDescriptorFallback) {
    var ERR_NON_OBJECT = 'Object.getOwnPropertyDescriptor called on a non-object: ';
    Object.getOwnPropertyDescriptor = function getOwnPropertyDescriptor(object, property) {
      if (isPrimitive(object)) {
        throw new TypeError(ERR_NON_OBJECT + object);
      }
      if (getOwnPropertyDescriptorFallback) {
        try {
          return getOwnPropertyDescriptorFallback.call(Object, object, property);
        } catch (err) {

        }
      }
      var descriptor;
      if (!owns(object, property)) {
        return descriptor;
      }
      descriptor = {
        enumerable: isEnumerable(object, property),
        configurable: true
      };
      if (supportsAccessors) {
        var prototype = object.__proto__;
        var notPrototypeOfObject = object !== prototypeOfObject;
        if (notPrototypeOfObject) {
          object.__proto__ = prototypeOfObject;
        }
        var getter = lookupGetter(object, property);
        var setter = lookupSetter(object, property);
        if (notPrototypeOfObject) {
          object.__proto__ = prototype;
        }
        if (getter || setter) {
          if (getter) {
            descriptor.get = getter;
          }
          if (setter) {
            descriptor.set = setter;
          }
          return descriptor;
        }
      }
      descriptor.value = object[property];
      descriptor.writable = true;
      return descriptor;
    };
  }

  var doesDefinePropertyWork = function doesDefinePropertyWork(object) {
    try {
      Object.defineProperty(object, 'sentinel', {});
      return 'sentinel' in object;
    } catch (exception) {
      return false;
    }
  };

  if (Object.defineProperty) {
    var definePropertyWorksOnObject = doesDefinePropertyWork({});
    var definePropertyWorksOnDom = typeof document === 'undefined' ||
      doesDefinePropertyWork(document.createElement('div'));
    if (!definePropertyWorksOnObject || !definePropertyWorksOnDom) {
      var definePropertyFallback = Object.defineProperty;
      var definePropertiesFallback = Object.defineProperties;
    }
  }

  if (!Object.defineProperty || definePropertyFallback) {
    var ERR_NON_OBJECT_DESCRIPTOR = 'Property description must be an object: ';
    var ERR_NON_OBJECT_TARGET = 'Object.defineProperty called on non-object: ';
    var ERR_ACCESSORS_NOT_SUPPORTED = 'getters & setters can not be defined on this javascript engine';
    Object.defineProperty = function defineProperty(object, property, descriptor) {
      if (isPrimitive(object)) {
        throw new TypeError(ERR_NON_OBJECT_TARGET + object);
      }
      if (isPrimitive(descriptor)) {
        throw new TypeError(ERR_NON_OBJECT_DESCRIPTOR + descriptor);
      }
      if (definePropertyFallback) {
        try {
          return definePropertyFallback.call(Object, object, property, descriptor);
        } catch (exception) {
        }
      }
      if ('value' in descriptor) {
        if (supportsAccessors && (lookupGetter(object, property) || lookupSetter(object, property))) {
          var prototype = object.__proto__;
          object.__proto__ = prototypeOfObject;
          delete object[property];
          object[property] = descriptor.value;
          object.__proto__ = prototype;
        } else {
          object[property] = descriptor.value;
        }
      } else {
        var hasGetter = 'get' in descriptor;
        var hasSetter = 'set' in descriptor;
        if (!supportsAccessors && (hasGetter || hasSetter)) {
          return object;
        }
        // If we got that far then getters and setters can be defined !!
        if (hasGetter) {
          defineGetter(object, property, descriptor.get);
        }
        if (hasSetter) {
          defineSetter(object, property, descriptor.set);
        }
      }
      return object;
    };
  }

  if (!Object.defineProperties || definePropertiesFallback) {
    Object.defineProperties = function defineProperties(object, properties) {
      if (definePropertiesFallback) {
        try {
          return definePropertiesFallback.call(Object, object, properties);
        } catch (exception) {
        }
      }
      var keys = Object.keys(properties);
      for (var i = 0; i < keys.length; i++) {
        var property = keys[i];
        if (property !== '__proto__') {
          Object.defineProperty(object, property, properties[property]);
        }
      }
      return object;
    };
  }

  if (!Object.create) {
    var createEmpty;
    var supportsProto = !({ __proto__: null } instanceof Object);
    /* global ActiveXObject */
    var shouldUseActiveX = function () {
      if (!document.domain) {
        return false;
      }
      try {
        return !!new ActiveXObject('htmlfile');
      } catch (exception) {
        return false;
      }
    };

    var getEmptyViaActiveX = function () {
      var empty;
      var xDoc;
      xDoc = new ActiveXObject('htmlfile');
      var script = 'script';
      xDoc.write('<' + script + '></' + script + '>');
      xDoc.close();

      empty = xDoc.parentWindow.Object.prototype;
      xDoc = null;

      return empty;
    };

    var getEmptyViaIFrame = function () {
      var iframe = document.createElement('iframe');
      var parent = document.body || document.documentElement;
      var empty;

      iframe.style.display = 'none';
      parent.appendChild(iframe);
      // eslint-disable-next-line no-script-url
      iframe.src = 'javascript:';

      empty = iframe.contentWindow.Object.prototype;
      parent.removeChild(iframe);
      iframe = null;

      return empty;
    };

    /* global document */
    if (supportsProto || typeof document === 'undefined') {
      createEmpty = function () {
        return { __proto__: null };
      };
    } else {
      createEmpty = function () {
        var empty = shouldUseActiveX() ? getEmptyViaActiveX() : getEmptyViaIFrame();

        delete empty.constructor;
        delete empty.hasOwnProperty;
        delete empty.propertyIsEnumerable;
        delete empty.isPrototypeOf;
        delete empty.toLocaleString;
        delete empty.toString;
        delete empty.valueOf;

        var Empty = function Empty() { };
        Empty.prototype = empty;
        // short-circuit future calls
        createEmpty = function () {
          return new Empty();
        };
        return new Empty();
      };
    }

    Object.create = function create (prototype, properties) {
      var object;
      var Type = function () { };
      if (prototype === null) {
        object = createEmpty();
      } else {
        if (prototype !== null && isPrimitive(prototype)) {
          throw new TypeError('Object prototype may only be an Object or null');
        }
        Type.prototype = prototype;
        object = new Type();
        object.__proto__ = prototype;
      }

      if (properties !== void 0) {
        Object.defineProperties(object, properties);
      }

      return object;
    };
  }

  if (!Object.seal) {
    Object.seal = function seal(object) {
      if (Object(object) !== object) {
        throw new TypeError('Object.seal can only be called on Objects.');
      }
      return object;
    };
  }

  if (!Object.freeze) {
    Object.freeze = function freeze(object) {
      if (Object(object) !== object) {
        throw new TypeError('Object.freeze can only be called on Objects.');
      }
      return object;
    };
  }

  try {
    Object.freeze(function () { });
  } catch (exception) {
    Object.freeze = (function (freezeObject) {
      return function freeze(object) {
        if (typeof object === 'function') {
          return object;
        }
        return freezeObject(object);
      };
    })(Object.freeze);
  }

  if (!Object.preventExtensions) {
    Object.preventExtensions = function preventExtensions(object) {
      if (Object(object) !== object) {
        throw new TypeError('Object.preventExtensions can only be called on Objects.');
      }
      return object;
    };
  }

  if (!Object.isSealed) {
    Object.isSealed = function isSealed(object) {
      if (Object(object) !== object) {
        throw new TypeError('Object.isSealed can only be called on Objects.');
      }
      return false;
    };
  }

  if (!Object.isFrozen) {
    Object.isFrozen = function isFrozen(object) {
      if (Object(object) !== object) {
        throw new TypeError('Object.isFrozen can only be called on Objects.');
      }
      return false;
    };
  }

  if (!Object.isExtensible) {
    Object.isExtensible = function isExtensible(object) {
      if (Object(object) !== object) {
        throw new TypeError('Object.isExtensible can only be called on Objects.');
      }
      var name = '';
      while (owns(object, name)) {
        name += '?';
      }
      object[name] = true;
      var returnValue = owns(object, name);
      delete object[name];
      return returnValue;
    };
  }
})();

/**
 * polyfill for Array
 */

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/isArray
if (!Array.isArray) {
  Array.isArray = function (arg) {
    return Object.prototype.toString.call(arg) === '[object Array]';
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/forEach
if (!Array.prototype.forEach) {
  Array.prototype.forEach = function (callback, thisArg) {
    var T, k;
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.forEach called on null or undefined');
    }
    var O = Object(this);
    var len = O.length >>> 0;
    if (Object.prototype.toString.call(callback) != '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    if (arguments.length > 1) {
      T = thisArg;
    }
    k = 0;
    while (k < len) {
      var kValue;
      if (k in O) {
        kValue = O[k];
        callback.call(T, kValue, k, O);
      }
      k++;
    }
  };
}

// form https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/every
if (!Array.prototype.every) {
  Array.prototype.every = function (callback) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.every called on null or undefined');
    }

    var O = Object(this);
    var len = O.length >>> 0;
    if (Object.prototype.toString.call(callback) != '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    var ctx = arguments.length >= 2 ? arguments[1] : void 0;
    for (var i = 0; i < len; i++) {
      if (i in O && !callback.call(ctx, O[i], i, O)) {
        return false;
      }
    }

    return true;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/fill
if (!Array.prototype.fill) {
  Array.prototype.fill = function (value) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.fill called on null or undefined');
    }
    var O = Object(this);
    var len = O.length >>> 0;
    var start = arguments[1];
    var relativeStart = start >> 0;
    var k = relativeStart < 0 ?
      Math.max(len + relativeStart, 0) :
      Math.min(relativeStart, len);

    var end = arguments[2];
    var relativeEnd = end === undefined ? len : end >> 0;
    var final = relativeEnd < 0 ? Math.max(len + relativeEnd, 0) : Math.min(relativeEnd, len);
    while (k < final) {
      O[k] = value;
      k++;
    }

    return O;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/filter
if (!Array.prototype.filter) {
  Array.prototype.filter = function (callback) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.filter called on null or undefined');
    }
    var O = Object(this);
    var len = O.length >>> 0;
    if (Object.prototype.toString.call(callback) != '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    var res = [];
    var ctx = arguments.length >= 2 ? arguments[1] : void 0;
    for (var i = 0; i < len; i++) {
      if (i in O) {
        var val = O[i];
        if (callback.call(ctx, val, i, O)) {
          res.push(val);
        }
      }
    }
    return res;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/indexOf
if (!Array.prototype.indexOf) {
  Array.prototype.indexOf = function (searchElement, fromIndex) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.indexOf called on null or undefined');
    }
    var k;
    var O = Object(this);
    var len = O.length >>> 0;
    if (len === 0) {
      return -1;
    }
    var n = fromIndex | 0;
    if (n >= len) {
      return -1;
    }
    k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);
    while (k < len) {
      if (k in O && O[k] === searchElement) {
        return k;
      }
      k++;
    }
    return -1;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/lastIndexOf
if (!Array.prototype.lastIndexOf) {
  Array.prototype.lastIndexOf = function(searchElement) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.lastIndexOf called on null or undefined');
    }
    var n, k;
    var t = Object(this);
    var len = t.length >>> 0;
    if (len === 0) {
      return -1;
    }
    n = len - 1;
    if (arguments.length > 1) {
      n = Number(arguments[1]);
      if (n !== n) {
        n = 0;
      } else if (n != 0 && n != (1 / 0) && n != -(1 / 0)) {
        n = (n > 0 || -1) * Math.floor(Math.abs(n));
      }
    }
    k = n >= 0 ? Math.min(n, len - 1) : len - Math.abs(n);
    for (;k >= 0; k--) {
      if (k in t && t[k] === searchElement) {
        return k;
      }
    }
    return -1;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/map
if (!Array.prototype.map) {
  Array.prototype.map = function (callback, thisArg) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.map called on null or undefined');
    }
    var T, A, k;
    var O = Object(this);
    var len = O.length >>> 0;
    if (Object.prototype.toString.call(callback) !== '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    if (thisArg) {
      T = thisArg;
    }
    A = new Array(len);
    k = 0;
    while (k < len) {
      var kValue, mappedValue;
      if (k in O) {
        kValue = O[k];
        mappedValue = callback.call(T, kValue, k, O);
        A[ k ] = mappedValue;
      }
      k++;
    }
    return A;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/Reduce
if (!Array.prototype.reduce) {
  Array.prototype.reduce = function (callback) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.reduce called on null or undefined');
    }
    if (Object.prototype.toString.call(callback) !== '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    var t = Object(this), len = t.length >>> 0, k = 0, value;
    if (arguments.length >= 2) {
      value = arguments[1];
    } else {
      while (k < len && !(k in t)) {
        k++;
      }
      if (k >= len) {
        throw new TypeError('Reduce of empty array with no initial value');
      }
      value = t[k++];
    }
    for (; k < len; k++) {
      if (k in t) {
        value = callback(value, t[k], k, t);
      }
    }
    return value;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/ReduceRight
if (!Array.prototype.reduceRight) {
  Array.prototype.reduceRight = function (callback) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.reduceRight called on null or undefined');
    }
    if (Object.prototype.toString.call(callback) !== '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    var t = Object(this), len = t.length >>> 0, k = len - 1, value;
    if (arguments.length >= 2) {
      value = arguments[1];
    } else {
      while (k >= 0 && !(k in t)) {
        k--;
      }
      if (k < 0) {
        throw new TypeError('Reduce of empty array with no initial value');
      }
      value = t[k--];
    }
    for (; k >= 0; k--) {
      if (k in t) {
        value = callback(value, t[k], k, t);
      }
    }
    return value;
  };
}

// from https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/some
if (!Array.prototype.some) {
  Array.prototype.some = function (callback) {
    if (this === void 0 || this === null) {
      throw new TypeError('Array.prototype.reduceRight called on null or undefined');
    }
    if (Object.prototype.toString.call(callback) !== '[object Function]') {
      throw new TypeError(callback + ' is not a function');
    }
    var t = Object(this);
    var len = t.length >>> 0;
    var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
    for (var i = 0; i < len; i++) {
      if (i in t && callback.call(thisArg, t[i], i, t)) {
        return true;
      }
    }
    return false;
  };
}

//----------------------------------------------------------------------
//
// CSSOM View Module
// https://dev.w3.org/csswg/cssom-view/
//
//----------------------------------------------------------------------

// Fix for IE8-'s Element.getBoundingClientRect()
(function (global) {
  if ('TextRectangle' in global && !('width' in global.TextRectangle.prototype)) {
    Object.defineProperties(global.TextRectangle.prototype, {
      'width': { get: function () { return this.right - this.left; } },
      'height': { get: function () { return this.bottom - this.top; } }
    });
  }
})(window);
/**
 * polyfill for Date
 */

// from https://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Global_Objects/Date/now
if (!Date.now) {
  Date.now = function now () {
    return new Date().getTime();
  };
}
// Production steps of ECMA-262, Edition 6, 22.1.2.1
// Reference: https://people.mozilla.org/~jorendorff/es6-draft.html#sec-array.from
if (!Array.from) {
    Array.from = (function () {
        var toStr = Object.prototype.toString;
        var isCallable = function (fn) {
            return typeof fn === 'function' || toStr.call(fn) === '[object Function]';
        };
        var toInteger = function (value) {
            var number = Number(value);
            if (isNaN(number)) { return 0; }
            if (number === 0 || !isFinite(number)) { return number; }
            return (number > 0 ? 1 : -1) * Math.floor(Math.abs(number));
        };
        var maxSafeInteger = Math.pow(2, 53) - 1;
        var toLength = function (value) {
            var len = toInteger(value);
            return Math.min(Math.max(len, 0), maxSafeInteger);
        };

        // The length property of the from method is 1.
        return function from(arrayLike/*, mapFn, thisArg */) {
            // 1. Let C be the this value.
            var C = this;

            // 2. Let items be ToObject(arrayLike).
            var items = Object(arrayLike);

            // 3. ReturnIfAbrupt(items).
            if (arrayLike == null) {
                throw new TypeError("Array.from requires an array-like object - not null or undefined");
            }

            // 4. If mapfn is undefined, then let mapping be false.
            var mapFn = arguments.length > 1 ? arguments[1] : void undefined;
            var T;
            if (typeof mapFn !== 'undefined') {
                // 5. else
                // 5. a If IsCallable(mapfn) is false, throw a TypeError exception.
                if (!isCallable(mapFn)) {
                    throw new TypeError('Array.from: when provided, the second argument must be a function');
                }

                // 5. b. If thisArg was supplied, let T be thisArg; else let T be undefined.
                if (arguments.length > 2) {
                    T = arguments[2];
                }
            }

            // 10. Let lenValue be Get(items, "length").
            // 11. Let len be ToLength(lenValue).
            var len = toLength(items.length);

            // 13. If IsConstructor(C) is true, then
            // 13. a. Let A be the result of calling the [[Construct]] internal method
            // of C with an argument list containing the single item len.
            // 14. a. Else, Let A be ArrayCreate(len).
            var A = isCallable(C) ? Object(new C(len)) : new Array(len);

            // 16. Let k be 0.
            var k = 0;
            // 17. Repeat, while k < len… (also steps a - h)
            var kValue;
            while (k < len) {
                kValue = items[k];
                if (mapFn) {
                    A[k] = typeof T === 'undefined' ? mapFn(kValue, k) : mapFn.call(T, kValue, k);
                } else {
                    A[k] = kValue;
                }
                k += 1;
            }
            // 18. Let putStatus be Put(A, "length", len, true).
            A.length = len;
            // 20. Return A.
            return A;
        };
    }());
}
// https://tc39.github.io/ecma262/#sec-array.prototype.findIndex
if (!Array.prototype.findIndex) {
    Object.defineProperty(Array.prototype, 'findIndex', {
        value: function(predicate) {
            // 1. Let O be ? ToObject(this value).
            if (this == null) {
                throw new TypeError('"this" is null or not defined');
            }

            var o = Object(this);

            // 2. Let len be ? ToLength(? Get(O, "length")).
            var len = o.length >>> 0;

            // 3. If IsCallable(predicate) is false, throw a TypeError exception.
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }

            // 4. If thisArg was supplied, let T be thisArg; else let T be undefined.
            var thisArg = arguments[1];

            // 5. Let k be 0.
            var k = 0;

            // 6. Repeat, while k < len
            while (k < len) {
                // a. Let Pk be ! ToString(k).
                // b. Let kValue be ? Get(O, Pk).
                // c. Let testResult be ToBoolean(? Call(predicate, T, « kValue, k, O »)).
                // d. If testResult is true, return k.
                var kValue = o[k];
                if (predicate.call(thisArg, kValue, k, o)) {
                    return k;
                }
                // e. Increase k by 1.
                k++;
            }

            // 7. Return -1.
            return -1;
        }
    });
}
// https://tc39.github.io/ecma262/#sec-array.prototype.find
if (!Array.prototype.find) {
    Object.defineProperty(Array.prototype, 'find', {
        value: function(predicate) {
            // 1. Let O be ? ToObject(this value).
            if (this == null) {
                throw new TypeError('"this" is null or not defined');
            }

            var o = Object(this);

            // 2. Let len be ? ToLength(? Get(O, "length")).
            var len = o.length >>> 0;

            // 3. If IsCallable(predicate) is false, throw a TypeError exception.
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }

            // 4. If thisArg was supplied, let T be thisArg; else let T be undefined.
            var thisArg = arguments[1];

            // 5. Let k be 0.
            var k = 0;

            // 6. Repeat, while k < len
            while (k < len) {
                // a. Let Pk be ! ToString(k).
                // b. Let kValue be ? Get(O, Pk).
                // c. Let testResult be ToBoolean(? Call(predicate, T, « kValue, k, O »)).
                // d. If testResult is true, return kValue.
                var kValue = o[k];
                if (predicate.call(thisArg, kValue, k, o)) {
                    return kValue;
                }
                // e. Increase k by 1.
                k++;
            }

            // 7. Return undefined.
            return undefined;
        },
        configurable: true,
        writable: true
    });
}
// from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/toISOString
if (!Date.prototype.toISOString) {
  (function() {
    function pad(number) {
      if (number < 10) {
        return '0' + number;
      }
      return number;
    }
    Date.prototype.toISOString = function () {
      return this.getUTCFullYear() +
        '-' + pad(this.getUTCMonth() + 1) +
        '-' + pad(this.getUTCDate()) +
        'T' + pad(this.getUTCHours()) +
        ':' + pad(this.getUTCMinutes()) +
        ':' + pad(this.getUTCSeconds()) +
        '.' + (this.getUTCMilliseconds() / 1000).toFixed(3).slice(2, 5) +
        'Z';
    };
  }());
}
/**
 * polyfill for DOM
 */

(function (global) {
  if (!('document' in global)) {
    return;
  }
  var document = global.document;

  // IE8- document.getElementsByClassName
  if (!document.getElementsByClassName && document.querySelectorAll) {
    var getElementsByClassName = function (classNames) {
      classNames = String(classNames).replace(/^|\s+/g, '.');
      return this.querySelectorAll(classNames);
    };
    void [HTMLDocument, Element].forEach(function (o) {
      o.prototype.getElementsByClassName = getElementsByClassName;
    });
  }

  // IE CustomEvent
  if (!('CustomEvent' in global) || typeof global.CustomEvent !== 'function') {
    var CustomEvent = function (event, params) {
      params = params || { bubbles: false, cancelable: false, detail: undefined };
      var evt = document.createEvent('CustomEvent');
      evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
      return evt;
    };
    CustomEvent.prototype = global.Event.prototype;
    global.CustomEvent = CustomEvent;
  }

  // Element.matches
  // from https://developer.mozilla.org/en/docs/Web/API/Element/matches
  (function () {
    if (!('Element' in global) || Element.prototype.matches) {
      return;
    }
    var matchesVenders = ['ms', 'o', 'moz', 'webkit'];
    var matchesSelectorSuffix = 'MatchesSelector';
    for (var i = 0; i < matchesVenders.length; i++) {
      var matchesSelector = matchesVenders[i] + matchesSelectorSuffix;
      if (matchesSelector in Element.prototype) {
        Element.prototype.matches = Element.prototype[matchesSelector];
        return;
      }
    }
    if (document.querySelectorAll) {
      Element.prototype.matches = function matches (selector) {
        var matches = (this.document || this.ownerDocument).querySelectorAll(selector);
        var i = matches.length;
        while (--i >= 0 && matches.item(i) !== this) {}
        return i > -1;
      };
    }
  })();

  // Node.textContent
  if (Object.defineProperty
    && Object.getOwnPropertyDescriptor
    && Object.getOwnPropertyDescriptor(Element.prototype, 'textContent')
    && !Object.getOwnPropertyDescriptor(Element.prototype, 'textContent').get) {
    (function() {
      var innerText = Object.getOwnPropertyDescriptor(Element.prototype, 'innerText');
      Object.defineProperty(Element.prototype, 'textContent', {
        get: function() {
          return innerText.get.call(this);
        },
        set: function(s) {
          return innerText.set.call(this, s);
        }
      });
    })();
  }
})(window);

/**
 * polyfill for DOM Event
 */

// from https://github.com/Financial-Times/polyfill-service/blob/master/polyfills/Event/polyfill.js
(function (global) {
  if (('Event' in global) && typeof global.Event === 'function') {
    return;
  }
  var unlistenableWindowEvents = {
    click: 1,
    dblclick: 1,
    keyup: 1,
    keypress: 1,
    keydown: 1,
    mousedown: 1,
    mouseup: 1,
    mousemove: 1,
    mouseover: 1,
    mouseenter: 1,
    mouseleave: 1,
    mouseout: 1,
    storage: 1,
    storagecommit: 1,
    textinput: 1
  };
  var existingProto = (global.Event && global.Event.prototype) || null;
  global.Event = Window.prototype.Event = function Event (type, eventInitDict) {
    if (!type) {
      throw new Error('Not enough arguments');
    }
    var event;
    if ('createEvent' in document) {
      event = document.createEvent('Event');
      var bubbles = eventInitDict && eventInitDict.bubbles !== undefined ? eventInitDict.bubbles : false;
      var cancelable = eventInitDict && eventInitDict.cancelable !== undefined ? eventInitDict.cancelable : false;
      event.initEvent(type, bubbles, cancelable);
      return event;
    }
    event = document.createEventObject();
    event.type = type;
    event.bubbles = eventInitDict && eventInitDict.bubbles !== undefined ? eventInitDict.bubbles : false;
    event.cancelable = eventInitDict && eventInitDict.cancelable !== undefined ? eventInitDict.cancelable : false;
    return event;
  };
  if (existingProto) {
    Object.defineProperty(global.Event, 'prototype', {
      configurable: false,
      enumerable: false,
      writable: true,
      value: existingProto
    });
  }
  if (!('createEvent' in document)) {
    var addEventListener = function (type, listener) {
      var element = this;
      if (element === global && type in unlistenableWindowEvents) {
        throw new Error('In IE8 the event: ' + type + ' is not available on the window object.');
      }
      if (!element._events) {
        element._events = {};
      }
      if (!element._events[type]) {
        element._events[type] = function (event) {
          var list = element._events[event.type].list;
          var events = list.slice();
          var index = -1;
          var length = events.length;
          var eventElement;
          event.preventDefault = function preventDefault () {
            if (event.cancelable !== false) {
              event.returnValue = false;
            }
          };
          event.stopPropagation = function stopPropagation () {
            event.cancelBubble = true;
          };
          event.stopImmediatePropagation = function stopImmediatePropagation () {
            event.cancelBubble = true;
            event.cancelImmediate = true;
          };
          event.currentTarget = element;
          event.target = event.target || event.srcElement || element;
          event.relatedTarget = event.fromElement ? (event.fromElement === event.target) ? event.toElement : event.fromElement : null;
          event.timeStamp = new Date().getTime();
          if (event.clientX) {
            event.pageX = event.clientX + document.documentElement.scrollLeft;
            event.pageY = event.clientY + document.documentElement.scrollTop;
          }
          while (++index < length && !event.cancelImmediate) {
            if (index in events) {
              eventElement = events[index];
              if (list.indexOf(eventElement) !== -1 && typeof eventElement === 'function') {
                eventElement.call(element, event);
              }
            }
          }
        };
        element._events[type].list = [];
        if (element.attachEvent) {
          element.attachEvent('on' + type, element._events[type]);
        }
      }
      element._events[type].list.push(listener);
    };

    var removeEventListener = function (type, listener) {
      var element = this;
      var index;
      if (element._events && element._events[type] && element._events[type].list) {
        index = element._events[type].list.indexOf(listener);
        if (index !== -1) {
          element._events[type].list.splice(index, 1);
          if (!element._events[type].list.length) {
            if (element.detachEvent) {
              element.detachEvent('on' + type, element._events[type]);
            }
            delete element._events[type];
          }
        }
      }
    };

    var dispatchEvent = function (event) {
      if (!arguments.length) {
        throw new Error('Not enough arguments');
      }
      if (!event || typeof event.type !== 'string') {
        throw new Error('DOM Events Exception 0');
      }
      var element = this, type = event.type;
      try {
        if (!event.bubbles) {
          event.cancelBubble = true;
          var cancelBubbleEvent = function (event) {
            event.cancelBubble = true;
            (element || window).detachEvent('on' + type, cancelBubbleEvent);
          };
          this.attachEvent('on' + type, cancelBubbleEvent);
        }
        this.fireEvent('on' + type, event);
      } catch (error) {
        event.target = element;
        do {
          event.currentTarget = element;
          if ('_events' in element && typeof element._events[type] === 'function') {
            element._events[type].call(element, event);
          }
          if (typeof element['on' + type] === 'function') {
            element['on' + type].call(element, event);
          }
          element = element.nodeType === 9 ? element.parentWindow : element.parentNode;
        } while (element && !event.cancelBubble);
      }
      return true;
    };

    void [Window, HTMLDocument, Element].forEach(function (o) {
      o.prototype.addEventListener = addEventListener;
      o.prototype.removeEventListener = removeEventListener;
      o.prototype.dispatchEvent = dispatchEvent;
    });

    // 添加DOMContentLoaded事件
    document.attachEvent('onreadystatechange', function() {
      if (document.readyState === 'complete') {
        document.dispatchEvent(new Event('DOMContentLoaded', {
          bubbles: true
        }));
      }
    });
  }
})(window);

/**
 * polyfill for getComputedStyle
 */

// from https://github.com/Financial-Times/polyfill-service/blob/master/polyfills/getComputedStyle/polyfill.js
(function (global) {
  if ('getComputedStyle' in global && typeof global.getComputedStyle === 'function') {
    return;
  }
  function getPixelSize (element, style, property, fontSize) {
    var sizeWithSuffix = style[property];
    var size = parseFloat(sizeWithSuffix);
    var suffix = sizeWithSuffix.split(/\d/)[0];
    var rootSize;
    fontSize = (fontSize !== null) ? fontSize :
      (/%|em/.test(suffix) && element.parentElement) ?
        getPixelSize(element.parentElement, element.parentElement.currentStyle, 'fontSize', null) : 16;
    rootSize = property === 'fontSize' ? fontSize :
      /width/i.test(property) ? element.clientWidth : element.clientHeight;
    return (suffix ==='em') ? size * fontSize :
      (suffix === 'in') ? size * 96 :
        (suffix === 'pt') ? size * 96 / 72 :
          (suffix === '%') ? size / 100 * rootSize : size;
  }

  function setShortStyleProperty (style, property) {
    var borderSuffix = property === 'border' ? 'Width' : '';
    var t = property + 'Top' + borderSuffix;
    var r = property + 'Right' + borderSuffix;
    var b = property + 'Bottom' + borderSuffix;
    var l = property + 'Left' + borderSuffix;
    style[property] = (style[t] == style[r] == style[b] == style[l] ? [style[t]]
      : style[t] == style[b] && style[l] == style[r] ? [style[t], style[r]]
      : style[l] == style[r] ? [style[t], style[r], style[b]]
      : [style[t], style[r], style[b], style[l]]).join(' ');
  }

  function CSSStyleDeclaration (element) {
    var currentStyle = element.currentStyle || {};
    var style = this;
    var fontSize = getPixelSize(element, currentStyle, 'fontSize', null);
    for (var property in currentStyle) {
      if (/width|height|margin.|padding.|border.+W/.test(property) && style[property] !== 'auto') {
        style[property] = getPixelSize(element, currentStyle, property, fontSize) + 'px';
      } else if (property === 'styleFloat') {
        style['float'] = currentStyle[property];
      } else {
        style[property] = currentStyle[property];
      }
    }
    setShortStyleProperty(style, 'margin');
    setShortStyleProperty(style, 'padding');
    setShortStyleProperty(style, 'border');
    style.fontSize = fontSize + 'px';
    return style;
  }

  CSSStyleDeclaration.prototype = {
    constructor: CSSStyleDeclaration,
    getPropertyPriority: function () { },
    getPropertyValue: function (prop) {
      return this[prop] || '';
    },
    item: function () { },
    removeProperty: function () { },
    setProperty: function () { },
    getPropertyCSSValue: function () { }
  };
  global.getComputedStyle = function (node) {
    return new CSSStyleDeclaration(node);
  };
})(window);
/**
 * polyfill for IE8 in HTML
 * https://html.spec.whatwg.org
 */

// document.head
document.head = document.head || document.getElementsByTagName('head')[0];

// HTML Tag shiv
void [
  'abbr', 'article', 'aside', 'audio', 'bdi', 'canvas', 'data', 'datalist',
  'details', 'dialog', 'figcaption', 'figure', 'footer', 'header', 'hgroup',
  'main', 'mark', 'meter', 'nav', 'output', 'picture', 'progress', 'section',
  'summary', 'template', 'time', 'video'
].forEach(function (tag) {
  document.createElement(tag);
});
/**
 * polyfill for String
 */

if (!String.prototype.trim) {
  String.prototype.trim = function () {
    return String(this).replace(/^\s+/, '').replace(/\s+$/, '');
  };
}
/**
 * Polyfill for Viewport
 */

(function (global) {
  if ('innerWidth' in global && 'innerHeight' in global && 'pageXOffset' in global && 'pageYOffset' in global) {
    return;
  }
  var doc = global.document;
  var docEl = doc.documentElement;
  var body = doc.body || doc.createElement('body');

  function scrollX () {
    return (docEl.scrollLeft || body.scrollLeft || 0) - (docEl.clientLeft || body.clientLeft || 0);
  }

  function scrollY () {
    return (docEl.scrollTop || body.scrollTop || 0) - (docEl.clientTop || body.clientTop || 0);
  }

  Object.defineProperties(global, {
    innerWidth: {
      get: function () {
        return docEl.clientWidth;
      }
    },
    innerHeight: {
      get: function () {
        return docEl.clientHeight;
      }
    },
    pageXOffset: {
      get: scrollX
    },
    pageYOffset: {
      get: scrollY
    },
    scrollX: {
      get: scrollX
    },
    scrollY: {
      get: scrollY
    }
  });
})(window);