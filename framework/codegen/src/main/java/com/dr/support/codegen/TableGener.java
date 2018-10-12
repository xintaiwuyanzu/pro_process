package com.dr.support.codegen;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import liquibase.snapshot.CachedRow;

public class TableGener {

    public void gen(String packageName, String targetDir, CachedRow cachedRow) {
        CompilationUnit cu = createCU(cachedRow, packageName);

        // prints the created compilation unit
        System.out.println(cu.toString());
    }

    private CompilationUnit createCU(CachedRow cachedRow, String packageName) {
        CompilationUnit cu = new CompilationUnit();
        // set the package
        cu.setPackageDeclaration(new PackageDeclaration(JavaParser.parseName(packageName)));
        // create the type declaration
        ClassOrInterfaceDeclaration type = cu.addClass(cachedRow.getString("TABLE_NAME"));
        return cu;
    }
}
