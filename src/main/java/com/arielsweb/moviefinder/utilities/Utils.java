/**
 * 
 */
package com.arielsweb.moviefinder.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Utility methods for Movie Finder Server
 * 
 * @author Ariel
 * @date 1/12/2011
 */
public class Utils {

    /**
     * This class will not be instantiated
     */
    private Utils() {
    }

    /**
     * For a given entity, say User, this method returns reflectively the name
     * of the table to which it is associated
     * 
     * @param clazz
     *            the Class of the entity
     * @return the name of the table
     */
    public static String getTableForEntity(Class<? extends Object> clazz) {
	Annotation tableAnnot = clazz.getAnnotation(Table.class);
	if (tableAnnot instanceof Table) {
	    Table tableAnnotation = (Table) tableAnnot;
	    return tableAnnotation.name();
	}

	if (tableAnnot == null) {
	    return clazz.getSimpleName().toUpperCase();
	}

	return MovieFinderConstants.STR_EMPTY;
    }

    public static String getColumnNameForField(Field field) {
	Annotation[] annotations = field.getDeclaredAnnotations();

	for (Annotation annotation : annotations) {
	    if (annotation instanceof Column) {
		Column columnAnnot = (Column) annotation;
		return columnAnnot.name();
	    }
	}

	return MovieFinderConstants.STR_EMPTY;
    }

}
