@echo off
echo === Graph Implementation Test Suite ===

echo.
echo 1. Testing ConcreteEdgesGraph...
java -ea -cp .;bin;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar org.junit.runner.JUnitCore graph.ConcreteEdgesGraphTest

echo.
echo 2. Testing ConcreteVerticesGraph...
java -ea -cp .;bin;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar org.junit.runner.JUnitCore graph.ConcreteVerticesGraphTest

echo.
echo 3. Testing GraphStaticTest...
java -ea -cp .;bin;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar org.junit.runner.JUnitCore graph.GraphStaticTest

echo.
echo === All tests completed ===
pause