# CouchGames test utils (work in progress)

A small Clojure library to simplify testing that I keep re-implementing over and over in my projects.

They have mostly to do with mutability (I use them to test code on the Java
Interop zone), but can be also useful when testing inherently mutable stuff 
(like functions returning dates or random numbers).

There is very little here right now, but I'm in a mind to expand it soon with more stuff.

## Usage

There is a bunch of examples in the `couchgames.test-utils-test` namespace.

### create-iterator

    (create-iterator seq)

Returns a function yielding each element of seq on each successive invocation
(nil is returned thereafter).

It can be useful when mocking Java code or functions that are not pure, like:

    (let [mock-function (create-iterator [1 2 3 4])]
        (is (= 1 (mock-function)))
        (is (= 2 (mock-function)))
        (is (= 3 (mock-function)))
        (is (= 4 (mock-function)))
        (is (nil? (mock-function))))

### with-checked-counter

    (with-checked-counter [name expected-count] & forms)

This macro executes the forms with a counter function bound to `name`, and then
asserts that the number of invocations matches the counter.

It is useful when you need to make sure one or more functions are invoked a set 
number of times, like this:

    (with-checked-counter [invoked 3]
        (let [some-object (reify SomeObject 
                              (someMethod [this] 
                                  (invoked) 
                                  42))]
            (.someMethod some-object)
            (.someMethod some-object)
            (.someMethod some-object)))

### with-checked-operations

    (with-checked-operations [name expected-id & options] forms)
    
Similar to `with-checked-counter` this macro executes the forms with a function
bound to `name`. The function in this case accepts a single operation ID 
parameter.

When all forms have been executed, the arguments passed to the `name` function
are compared with the vector `expected-id` (by default without accounting the 
order, unless the `:ordered` option is passed) and if they match, the test succeeds.

This example passes:

    (with-checked-operations [did-it [:a :b :c] :ordered]
        (let [someObject (reify SomeObject
                             (methodA [this] (did-it :a))
                             (methodB [this] (did-it :b))
                             (methodC [this] (did-it :c)))]
            (.methodA someObject)
            (.methodB someObject)
            (.methodC someObject))
                                         
## License

Copyright © 2019 Riccardo Di Meo

THIS WORK IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

Distribution or inclusion of this work as a compiled jar not including the source
code is provided under the Creative Commons CC0 1.0 license, which is available
here: https://creativecommons.org/publicdomain/zero/1.0/legalcode

Other forms of distributions are covered under the terms of the Creative
Commons "Attribution 4.0 International" license, which is available at 
https://creativecommons.org/licenses/by/4.0/legalcode 



