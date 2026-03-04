import { useNavigate } from 'react-router-dom';
import type { ListProps, Novel } from '../../types/book';
import { getBookEmoji, renderStars } from '../../utils/novelUtil';
import ReactPaginate from 'react-paginate';

const NovelList = ({
  dtoList,
  toggle,
  handlePageClick,
  pageCount,
  currentPage,
}: ListProps) => {
  const navigate = useNavigate();

  return (
    <>
      <section className="p-0">
        {dtoList.map((novel: Novel) => (
          <article
            className="mb-2.5 flex items-center rounded-[5px] border-2 border-stone-200 bg-white p-4"
            key={novel.id}
          >
            <div className="pr-4 first:text-5xl">{getBookEmoji(novel.id)}</div>
            <div className="grow">
              <h3>{novel.title}</h3>
              <p>
                {novel.author} / {novel.genreName}
              </p>
              <p className="text-2xl text-sky-400">
                {renderStars(novel.rating)}
              </p>
            </div>
            <div className="flex flex-col text-[0.9em]">
              <button
                className={`m-1 w-25 rounded-[3px] bg-sky-500 py-2.5 text-center text-white hover:bg-sky-700`}
                onClick={() => toggle(novel.id, novel.available)}
              >
                {novel.available ? 'Available' : 'Not Available'}
              </button>
              <button
                onClick={() => navigate(`/novels/${novel.id}`)}
                className="m-1 w-25 rounded-[3px] bg-stone-100 px-1.5 py-3 text-center"
              >
                Details
              </button>
            </div>
          </article>
        ))}
      </section>
      <ReactPaginate
        breakLabel="..."
        nextLabel="next >"
        onPageChange={handlePageClick}
        pageRangeDisplayed={5}
        pageCount={pageCount}
        previousLabel="< previous"
        renderOnZeroPageCount={null}
        forcePage={currentPage}
        containerClassName="flex justfy-center gap-2 mt-4"
        pageClassName="px-3 py-1 border rounded cursor-pointer"
        activeClassName="bg-orange-500 text-white"
        previousClassName="px-3 py-1 border rounded cursor-pointer"
        nextClassName="px-3 py-1 border rounded cursor-pointer"
      />
    </>
  );
};

export default NovelList;
