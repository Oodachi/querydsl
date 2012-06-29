/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.jpa;

import java.util.Collection;

import com.mysema.query.Query;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.MapExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * JPQLCommonQuery is a common interface for HQLQuery and HQLSubQuery
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public interface JPQLCommonQuery<Q extends JPQLCommonQuery<Q>>  extends Query<Q> {
   
    /**
     * Set the sources of this query
     *
     * @param sources
     * @return
     */
    Q from(EntityPath<?>... sources);

    /**
     * Create a inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(EntityPath<P> target);

    /**
     * Create a inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create a inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(Path<? extends Collection<P>> target);

    /**
     * Create a inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create a inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(MapExpression<?, P> target);

    /**
     * Create a inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create a join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(EntityPath<P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create a join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(Path<? extends Collection<P>> target);

    /**
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create a join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(MapExpression<?, P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create a left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(EntityPath<P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create a left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(Path<? extends Collection<P>> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create a left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(MapExpression<?, P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(MapExpression<?, P> target, Path<P> alias);
    
    /**
     * Create a right join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q rightJoin(EntityPath<P> target);

    /**
     * Create a right join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q rightJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create a right join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q rightJoin(Path<? extends Collection<P>> target);

    /**
     * Create a right join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q rightJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create a right join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q rightJoin(MapExpression<?, P> target);

    /**
     * Create a right join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q rightJoin(MapExpression<?, P> target, Path<P> alias);


    /**
     * Create a full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(EntityPath<P> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create a full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(Path<? extends Collection<P>> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create a full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(MapExpression<?, P> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Add join conditions to the last added join
     *
     * @param condition
     * @return
     */
    Q with(Predicate... condition);

}
