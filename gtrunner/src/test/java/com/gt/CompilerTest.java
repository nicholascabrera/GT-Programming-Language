package com.gt;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gt.service.Compiler;

/**
 * Unit test for simple App.
 */
public class CompilerTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void compilerTest(){
        assertTrue((new com.gt.service.Compiler("code")).compile());
    }

    @Test
    public void inputTest(){
        Compiler compiler = new Compiler("in");
        compiler.compile();
        assertTrue(compiler.getAdjacencyList().getList().equals("[E1A:4]"));
    }

    @Test
    public void outputTest(){
        Compiler compiler = new Compiler("out");
        compiler.compile();
        assertTrue(compiler.getAdjacencyList().getList().equals("[I1E]"));
    }

    @Test
    public void commentTest(){
        Compiler compiler = new Compiler("comment");
        compiler.compile();
        assertTrue(compiler.getAdjacencyList().getList().equals("[N1N: this is a comment.]"));
    }

    @Test
    public void newTest(){
        Compiler compiler = new Compiler("new");
        compiler.compile();
        assertTrue(compiler.getAdjacencyList().getList().equals("[I1node:5, I2array1:[0, 1, 2]]"));
    }

    @Test
    public void mathTest(){
        Compiler compiler = new Compiler("math");
        compiler.compile();
        assertTrue(compiler.getAdjacencyList().getList().equals("[M1node:(node*-1)*3]"));
    }

    @Test
    public void logicTest(){
        Compiler compiler = new Compiler("logic");
        compiler.compile();
        String ajaList = "[I1node:1, N2D:node < 2, N3K, N4I, M5node:(node*2)^3, N6E:4, N7L, " + 
            "N8D:node > 3, N9K, N10I, M11node:(node*-1)*3, N12D:1 > 2, N13K, N14I, M15node:(node*-2)*3," +
            " N16E:14, N17L, N18I, M19node:(node*-3)*3, N20E:18, M21node:(node*-1)*3, N22E:10, N23L, N24I, M25node:4, N26E:24, N27node:4 < node]";
        assertTrue(compiler.getAdjacencyList().getList().equals(ajaList));
    }

    @Test
    public void branchTest(){
        Compiler compiler = new Compiler("branch");
        compiler.compile();
        String ajaList = "[I1iterator:0, N2F:label, M3iterator:iterator + 1, N4D:iterator < 10, N5K, N6I, I7E, N8B:label, N9N:branch test, N10E:6]";
        assertTrue(compiler.getAdjacencyList().getList().equals(ajaList));
    }
}