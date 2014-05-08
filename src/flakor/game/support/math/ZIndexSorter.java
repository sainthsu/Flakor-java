package flakor.game.support.math;

import flakor.game.core.entity.EntityInterface;

import java.util.Comparator;
import java.util.List;

public class ZIndexSorter extends InsertionSorter<EntityInterface>
{
	// ===========================================================
		// Constants
		// ===========================================================

		private static ZIndexSorter INSTANCE;

		// ===========================================================
		// Fields
		// ===========================================================

		private final Comparator<EntityInterface> ZIndexComparator = new Comparator<EntityInterface>()
		{
			@Override
			public int compare(final EntityInterface entityA, final EntityInterface entityB)
			{
				return entityA.getZIndex() - entityB.getZIndex();
			}
		};

		// ===========================================================
		// Constructors
		// ===========================================================

		private ZIndexSorter()
		{

		}

		public static ZIndexSorter getInstance()
		{
			if(INSTANCE == null)
			{
				INSTANCE = new ZIndexSorter();
			}
			return INSTANCE;
		}


		// ===========================================================
		// Methods
		// ===========================================================

		public void sort(final EntityInterface[] entities)
		{
			this.sort(entities, this.ZIndexComparator);
		}

		public void sort(final EntityInterface[] entities, final int start, final int end) 
		{
			this.sort(entities, start, end, this.ZIndexComparator);
		}

		public void sort(final List<EntityInterface> entities)
		{
			this.sort(entities, this.ZIndexComparator);
		}

		public void sort(final List<EntityInterface> entities, final int start, final int end)
		{
			this.sort(entities, start, end, this.ZIndexComparator);
		}

		public void sort(final ListInterface<EntityInterface> entities) 
		{
			this.sort(entities, this.ZIndexComparator);
		}

		public void sort(final ListInterface<EntityInterface> entities, final int start, final int end)
		{
			this.sort(entities, start, end, this.ZIndexComparator);
		}

}
