package site.remlit.aster.util.sql

import org.jetbrains.exposed.v1.core.ComparisonOp
import org.jetbrains.exposed.v1.core.Expression

/**
 * Comparison operation for if an array contains the result of an expression.
 *
 * @param expr1 Left expression
 * @param expr2 Right expression
 *
 * @since 2025.10.5.0-SNAPSHOT
 * */
class ArrayContainsOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "@>")